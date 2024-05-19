package io.jay.service.repository.jpa;

import io.jay.service.entity.jpa.TeamJPA;
import io.jay.service.model.projection.TeamView;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamJPARepository extends JpaRepository<TeamJPA, Long> {

    // If project have a list relation object,
    // It must use fetch join to get data.And it will select all column
    @EntityGraph(attributePaths = {"members.id", "members.name"})
    @Query(value =
            """
                        SELECT t
                        FROM TeamJPA t
                        LEFT JOIN FETCH t.members m
                    """)
    List<TeamView> findAllUsingJoinFetch();

    /**
     * You need to manual group by in java code
     *
     * @return
     */
    @Query(value =
            """
                        SELECT t.id, t.name,
                            NEW io.jay.service.model.projection.MemberDTO(m.id as memberId, m.name as memberName)
                        FROM TeamJPA t
                        LEFT JOIN t.members m
                    """)
    List<Object[]> findAllNotUsingJoinFetch();

    @Lock(LockModeType.PESSIMISTIC_READ)
//    @QueryHints(@QueryHint(name = "javax.persistence.lock.timeout",value = "0"))
    // Only work for supported DB (If not , it will use pessimistic write instead as H2 DB)
    @Query(value = "Select t from TeamJPA t where t.id = :teamId ")
    Optional<TeamJPA> findByIdWithPessimisticRead(Long teamId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "Select t from TeamJPA t where t.id = :teamId ")
    Optional<TeamJPA> findByIdWithPessimisticWrite(Long teamId);

    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    @Query(value = "Select t from TeamJPA t where t.id = :teamId ")
    Optional<TeamJPA> findByIdWithPessimisticForce(Long teamId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "Select t from TeamJPA t where t.id = :teamId ")
    Optional<TeamJPA> findByIdWithOptimistic(Long teamId);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query(value = "Select t from TeamJPA t where t.id = :teamId ")
    Optional<TeamJPA> findByIdWithOptimisticForce(Long teamId);

    @Modifying
    @Query(value = "UPDATE teams set name = :name where id = :id", nativeQuery = true)
    int updateTeamJPA(Long id, String name);

}
