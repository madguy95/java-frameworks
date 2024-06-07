package io.jay.service.mongo;

import com.github.javafaker.Faker;
import io.jay.service.mongo.document.Address;
import io.jay.service.mongo.document.Player;
import io.jay.service.mongo.document.PlayerPosition;
import io.jay.service.mongo.document.TeamMg;
import io.jay.service.mongo.repository.PlayerRepository;
import io.jay.service.mongo.repository.TeamMgRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class DatabaseSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final TeamMgRepository teamMgRepository;

    private final PlayerRepository playerRepository;

    private final Faker faker;

    public DatabaseSeeder(TeamMgRepository teamMgRepository, PlayerRepository playerRepository) {
        this.teamMgRepository = teamMgRepository;
        this.playerRepository = playerRepository;
        this.faker = new Faker(Locale.FRANCE);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        teamMgRepository.deleteAll();
        playerRepository.deleteAll();

        Set<String> teamNames = new HashSet<>();
        int counter = 0;

        while (counter < 10) {
            String teamName = faker.team().name();

            if (!teamNames.contains(teamName)) {
                teamNames.add(teamName);
                counter++;
            }
        }

        for(String teamName: teamNames) {
            createAndPersistTeam(teamName);
        }
    }

    private void createAndPersistTeam(String teamName) {
        List<Player> players = new ArrayList<>() {{
            add(createPlayer());
            add(createPlayer());
            add(createPlayer());
            add(createPlayer());
            add(createPlayer());
            add(createPlayer());
            add(createPlayer());
            add(createPlayer());
            add(createPlayer());
            add(createPlayer());
            add(createPlayer());
        }};

        List<Player> createdPlayers = playerRepository.saveAll(players);

        Address address = new Address(
                faker.address().city(),
                faker.address().zipCode(),
                faker.address().streetAddress()
        );

        TeamMg teamMg = new TeamMg()
                .setName(teamName)
                .setAcronym(teamName.replaceAll(" ", "").toUpperCase())
                .setAddress(address)
                .setPlayers(new HashSet<>(createdPlayers));

        teamMgRepository.save(teamMg);
    }

    private Player createPlayer() {
        PlayerPosition[] positions = PlayerPosition.toArray();

        return new Player()
                .setName(faker.name().firstName() + " " + faker.name().lastName())
                .setBirthDate(Instant.ofEpochMilli(faker.date().birthday(18, 38).getTime()).atZone(ZoneId.systemDefault())
                        .toLocalDate())
                .setPosition(positions[faker.random().nextInt(0, positions.length - 1)])
                .setAvailable(faker.random().nextBoolean());
    }
}
