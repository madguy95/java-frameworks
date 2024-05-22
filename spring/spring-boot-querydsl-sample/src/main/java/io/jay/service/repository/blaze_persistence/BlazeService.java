package io.jay.service.repository.blaze_persistence;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.EscapeBuilder;
import com.blazebit.persistence.criteria.*;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import io.jay.service.entity.Member;
import io.jay.service.entity.Member_;
import io.jay.service.entity.Team;
import io.jay.service.entity.Team_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlazeService {

    private final CriteriaBuilderFactory builderFactory;

    private static final CharSequence PER = "%";
    private final EntityManager entityManager;
    private final EntityViewManager evm;

    /**
     * Using criteria join
     *
     * @return
     */
    public List<TeamView> retrieveTeamsByCriteria(Long id, String name, String memberName) {
//        EntityViewConfiguration config = EntityViews.createDefaultConfiguration();
//        config.addEntityView(TeamView.class);
//        EntityViewManager evm = config.createEntityViewManager(builderFactory);
        BlazeCriteriaBuilder criteriaBuilder = BlazeCriteria.get(builderFactory);
        BlazeCriteriaQuery<Team> criteriaQuery = criteriaBuilder.createQuery(Team.class);
        BlazeRoot<Team> root = criteriaQuery.from(Team.class);
        BlazeJoin<Team, Member> joinMember = root.join(Team_.MEMBERS);
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
        CriteriaBuilder<Team> builder = criteriaQuery.createCriteriaBuilder(entityManager);
        List<TeamView> result = evm.applySetting(
                EntityViewSetting.create(TeamView.class), builder).getResultList();

        // Convert the result to list of CustomProjection
        return result;
    }

    /**
     * Using join in entity view and add condition by alias
     *
     * @return
     */
    public List<TeamView> retrieveTeamsByFetchAlias(Long id, String name, String memberName) {
        CriteriaBuilder cb = builderFactory.create(entityManager, Team.class, "t");
        if (id != null) {
            cb.where("t.id").eq(id);
        }
        if (name != null) {
            ((EscapeBuilder) cb.where("LOWER(t.name)").like().value(
                    String.join("", PER, StringUtils.lowerCase(name), PER))).noEscape();
        }
        if (memberName != null) {
            ((EscapeBuilder) cb.where("LOWER(t.members.name)").like().value(
                    String.join("", PER, StringUtils.lowerCase(memberName), PER))).noEscape();
        }
        List<TeamView> result = evm.applySetting(
                EntityViewSetting.create(TeamView.class), cb).getResultList();

        // Convert the result to list of CustomProjection
        return result;
    }

    /**
     * Using join and select only specific columns
     *
     * @return
     */
    public List<TeamView.JoinTeamView> retrieveTeamsByEntityView(Long id, String name, String memberName) {
        CriteriaBuilder cb = builderFactory.create(entityManager, Team.class, "t");
        if (id != null) {
            cb.where("t.id").eq(id);
        }
//        if (name != null) {
//            ((EscapeBuilder) cb.where("LOWER(t.name)").like().value(
//                    String.join("", PER, StringUtils.lowerCase(name), PER))).noEscape();
//        }
//        if (memberName != null) {
//            ((EscapeBuilder) cb.where("LOWER(t.members.name)").like().value(
//                    String.join("", PER, StringUtils.lowerCase(memberName), PER))).noEscape();
//        }
        EntityViewSetting setting = EntityViewSetting.create(TeamView.JoinTeamView.class);
        setting.addAttributeFilter("teamName", "name", name);
//        setting.addAttributeFilter("listMembers", "memberName", memberName); // not support
        List<TeamView.JoinTeamView> result = evm.applySetting(
                setting, cb).getResultList();

        // Convert the result to list of CustomProjection
        return result;
    }
}
