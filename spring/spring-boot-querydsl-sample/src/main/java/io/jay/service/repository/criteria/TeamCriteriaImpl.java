package io.jay.service.repository.criteria;

import io.jay.service.entity.*;
import io.jay.service.model.projection.MemberDTO;
import io.jay.service.model.projection.TeamDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Repository
@RequiredArgsConstructor
public class TeamCriteriaImpl implements TeamCriteriaRepository {
    private static final CharSequence PER = "%";
    private final EntityManager entityManager;

    /**
     * Using fetch join
     *
     * @return
     */
    @Override
    public List<TeamDTO> retrieveTeamsByCriteria(Long id, String name, String memberName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Team> criteriaQuery = criteriaBuilder.createQuery(Team.class);
        Root<Team> root = criteriaQuery.from(Team.class);
        root.fetch(Team_.MEMBERS);
        List<Predicate> lstPredicates = new ArrayList<>();
        if (id != null) {
            lstPredicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(Team_.ID), id)));
        }
        if (name != null) {
            lstPredicates.add(criteriaBuilder.and(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Team_.NAME)),
                    String.join("", PER, StringUtils.lowerCase(name), PER))));
        }
        if (memberName != null) {
            lstPredicates.add(criteriaBuilder.and(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Team_.MEMBERS).get(Member_.NAME)),
                    String.join("", PER, StringUtils.lowerCase(memberName), PER))));
        }
        criteriaQuery.where(criteriaBuilder.and(lstPredicates.toArray(new Predicate[]{})));
        List<TeamDTO> result = entityManager.createQuery(criteriaQuery).getResultList().stream().map(team ->
                new TeamDTO(team.getId(), team.getName(),
                        team.getMembers().stream().map(
                                member -> new MemberDTO(member.getId(), member.getName())).toList())).collect(
                Collectors.toList());

        // Convert the result to list of CustomProjection
        return result;
    }

    /**
     * Using join and select only specific columns
     *
     * @return
     */
    @Override
    public List<TeamDTO> retrieveTeamsByCriteriaProjection(Long id, String name, String memberName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // You can return to a list TeamDTO object
//        CriteriaQuery<TeamDTO> criteriaQuery = criteriaBuilder.createQuery(TeamDTO.class);
        // Here, I want demo subquery in select, so it require return array Object
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Team> root = criteriaQuery.from(Team.class);
        Join joinMember = root.join(Team_.members, JoinType.LEFT);
        List<Predicate> lstPredicates = new ArrayList<>();
        if (id != null) {
            lstPredicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(Team_.ID), id)));
        }
        if (name != null) {
            lstPredicates.add(criteriaBuilder.and(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Team_.NAME)),
                    String.join("", PER, StringUtils.lowerCase(name), PER))));
        }
        if (memberName != null) {
            lstPredicates.add(criteriaBuilder.and(criteriaBuilder.like(
                    criteriaBuilder.lower(joinMember.get(Member_.NAME)),
                    String.join("", PER, StringUtils.lowerCase(memberName), PER))));
        }
        criteriaQuery.where(criteriaBuilder.and(lstPredicates.toArray(new Predicate[]{})));
        Subquery<Long> subQuery = criteriaQuery.subquery(Long.class);
        Root<Milestone> subRoot = subQuery.from(Milestone.class);
        subQuery.select(criteriaBuilder.count(subRoot.get(Milestone_.ID)));
        subQuery.where(criteriaBuilder.equal(root.get(Team_.ID), subRoot.get(Milestone_.ID)));
        criteriaQuery.multiselect(
                subQuery.getSelection(),
                criteriaBuilder.construct(TeamDTO.class,
                        root.get(Team_.ID), root.get(Team_.NAME),
                        criteriaBuilder.construct(MemberDTO.class,
                                joinMember.get(Member_.ID).alias("memberId"),
                                joinMember.get(Member_.NAME).alias("memberName")))
        );
        List<TeamDTO> result = entityManager.createQuery(criteriaQuery).getResultList().stream()
                .map(tuple -> (TeamDTO) tuple[1])
                .collect(
                        Collectors.groupingBy(tuple -> tuple.getId()))
                .entrySet().stream().map(
                        tuple -> {
                            TeamDTO sameTeam = tuple.getValue().get(0);
                            return new TeamDTO(sameTeam.getId(), sameTeam.getName(),
                                    tuple.getValue().stream().map(
                                            t -> t.getMemberDTO()).toList());
                        }).toList();

        // Convert the result to list of CustomProjection
        return result;
    }
}
