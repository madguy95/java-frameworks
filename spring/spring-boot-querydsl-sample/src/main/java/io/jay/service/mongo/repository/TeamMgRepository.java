package io.jay.service.mongo.repository;

import io.jay.service.mongo.document.TeamMg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMgRepository extends MongoRepository<TeamMg, String> {
    List<TeamMg> findByNameOrderByNameDesc();

    List<TeamMg> findByIdIsNotNullOrderByNameDesc();

    List<TeamMg> findByNameContainingIgnoreCaseOrderByNameDesc(String nameKeyword);

    List<TeamMg> findByAddressCityIgnoreCase(String city);
}
