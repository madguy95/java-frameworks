package io.jay.service.repository;

import io.jay.service.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("""
            select distinct t
                from Team t
                left join fetch t.members
            """)
    List<Member> findAllUsingJoinFetch();


}
