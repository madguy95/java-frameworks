package io.jay.service.repository.jpa;

import io.jay.service.entity.nonlock.TeamNoLock;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamNoLockRepository extends JpaRepository<TeamNoLock, Long> {

    @Modifying
    @Query(value = "UPDATE teams set name = :name where id = :id", nativeQuery = true)
    int updateTeamJPA(Long id, String name);

    List<TeamNoLock> findAllByName(String name);
    @QueryHints(@QueryHint(name = "javax.persistence.lock.timeout",value = "5"))
    TeamNoLock saveAndFlush(TeamNoLock entity);

}
