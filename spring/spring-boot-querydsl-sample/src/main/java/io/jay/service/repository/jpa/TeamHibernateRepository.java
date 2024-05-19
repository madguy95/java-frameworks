package io.jay.service.repository.jpa;

import io.jay.service.entity.hibernate.TeamHibernate;
import io.jay.service.model.projection.TeamView;
import io.jay.service.model.projection.TeamDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamHibernateRepository extends JpaRepository<TeamHibernate, Long> {

    @Query(value = """
            SELECT t.id as id, t.name as name, mh.name as memberName
            FROM TeamHibernate t
                LEFT JOIN (SELECT m.name as name, m.teamId as teamId 
                                FROM MemberHibernate m) AS mh ON t.id = mh.teamId
            """)
    List<TeamView.TeamSubView> findAllWithSubQuery();


    @Query(value = """
            SELECT t
                FROM TeamHibernate t
                LEFT JOIN FETCH t.members m
                WHERE t.id = :id 
                    and (:name is null OR lower(t.name) like '%' || :name || '%' )
                    and (:memberName is null OR lower(m.name) like '%' || :memberName || '%')
            """)
    List<TeamView> findAllInterfaceProjection(Long id, String name, String memberName);

    @Query(value = """
            SELECT new io.jay.service.model.projection.TeamDTO(t.id as id, t.name as name, 
                        new io.jay.service.model.projection.MemberDTO(m.id as memberId, m.name as memberName))
                FROM TeamHibernate t
                LEFT JOIN t.members m
                WHERE t.id = :id 
                    and (:name is null OR lower(t.name) like '%' || :name || '%' )
                    and (:memberName is null OR lower(m.name) like '%' || :memberName || '%')
            """)
    List<TeamDTO> findAllClassProjection(Long id, String name, String memberName);
}
