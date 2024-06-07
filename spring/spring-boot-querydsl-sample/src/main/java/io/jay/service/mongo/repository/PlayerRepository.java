package io.jay.service.mongo.repository;

import io.jay.service.mongo.document.Player;
import io.jay.service.mongo.document.PlayerPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
    List<Player> findByPositionAndIsAvailable(PlayerPosition playerPosition, boolean isAvailable);

    List<Player> findDistinctNameByPositionIn(List<PlayerPosition> playerPositions);

    List<Player> findByBirthDateIsBetweenOrderByBirthDate(LocalDate fromDate, LocalDate toDate);

    Player findFirstByOrderByBirthDateDesc();

    List<Player> findFirst10ByOrderByBirthDate();

    Page<Player> findByIdIsNotNull(Pageable pageable);
}
