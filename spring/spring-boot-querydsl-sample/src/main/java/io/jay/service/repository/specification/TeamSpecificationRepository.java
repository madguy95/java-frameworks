package io.jay.service.repository.specification;

import io.jay.service.entity.jpa.TeamJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamSpecificationRepository extends JpaRepository<TeamJPA, Long>, JpaSpecificationExecutor<TeamJPA> {
}
