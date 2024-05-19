package io.jay.service.repository.specification;

import io.jay.service.entity.jpa.TeamJPA;
import io.jay.service.model.projection.TeamView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TeamSpecificationImpl {

    private final TeamSpecificationRepository teamSpecificationRepository;

    public List<TeamJPA> findTeamByID(Long id) {
        return teamSpecificationRepository.findAll(TeamSpecification.withId(id));
    }

    public List<TeamJPA> findTeamByIDAndName(Long id, String name, String memberName) {
        return teamSpecificationRepository.findAll(Specification.allOf(
                TeamSpecification.withMember(memberName), // using fetch join to get members
                TeamSpecification.withId(id),
                TeamSpecification.withName(name)));
    }

    /**
     * Restriction: Generated SQL will query all column (it's not necessary, not performance)
     * <p>
     * Specification will only define the where clause of a query (so any select change is not value)
     */
    public List<TeamView> findTeamByIDAndNameProjection(Long id, String name, String memberName) {
        return teamSpecificationRepository.findBy(Specification.allOf(
                TeamSpecification.withMember(memberName), // using fetch join to get members
                TeamSpecification.withId(id),
                TeamSpecification.withName(name)), q -> q.as(TeamView.class).all());
    }


}
