package io.jay.service.repository.jpa.hibernate;

import io.jay.service.model.projection.TeamView;
import io.jay.service.model.projection.TeamDTO;
import io.jay.service.repository.jpa.TeamHibernateRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamHibernateImpl {

    private static final CharSequence PER = "%";
    private SessionFactory sessionFactory;

    private final EntityManager entityManager;

    private final TeamHibernateRepository teamHibernateRepository;

    public List<TeamView.TeamSubView> findAllDefault() {
        List<TeamView.TeamSubView> result = teamHibernateRepository.findAllWithSubQuery();
//        result.stream().forEach((el) -> entityManager.detach(el));
        return result;
    }

    /**
     * @param id
     * @param name
     * @param member
     * @return
     */
    public List<TeamView> findTeamByIDAndName(Long id, String name, String member) {
        sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        try (Session session = sessionFactory.openSession()) {
            // set result type to projection but not valuable. It still returns a list entities in deep
            Query query = session.createNamedQuery("FIND_TEAMS", TeamView.class);
            query.setParameter("id", id);
            query.setParameter("name", StringUtils.lowerCase(name));
            query.setParameter("memberName", StringUtils.lowerCase(member));
            List<TeamView> resultList = query.getResultList();
            return resultList;
            // commit transaction
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param id
     * @param name
     * @param member
     * @return
     */
    public List<TeamView> findTeamByIDAndNameInterface(Long id, String name, String member) {
        return teamHibernateRepository.findAllInterfaceProjection(id, StringUtils.lowerCase(name),
                StringUtils.lowerCase(member));
    }

    /**
     * @param id
     * @param name
     * @param memberName
     * @return
     */
    public List<TeamDTO> findTeamByIDAndNameProjection(Long id, String name, String memberName) {
        return teamHibernateRepository.findAllClassProjection(id, StringUtils.lowerCase(name),
                StringUtils.lowerCase(memberName)).stream().collect(
                Collectors.groupingBy(TeamDTO::getId,
                        Collectors.mapping(el -> el, Collectors.toList()))
        ).entrySet().stream().map(entry -> {
            List<TeamDTO> lst = entry.getValue();
            return new TeamDTO(lst.get(0).getId(), lst.get(0).getName(),
                    lst.stream().map(el -> el.getMemberDTO()).collect(Collectors.toList()));
        }).toList();
    }


    public List<TeamDTO> findAllUsingNamedSubQuery() {
        sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createNamedQuery("FIND_TEAMS_ONLY", TeamDTO.class);
            return query.getResultList();

            // commit transaction
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
