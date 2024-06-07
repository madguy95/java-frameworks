package io.jay.service.mongo;

import io.jay.service.mongo.document.Player;
import io.jay.service.mongo.document.PlayerPosition;
import io.jay.service.mongo.document.TeamMg;
import io.jay.service.mongo.dto.CreatePlayerDto;
import io.jay.service.mongo.dto.CreateTeamDto;
import io.jay.service.mongo.repository.PlayerRepository;
import io.jay.service.mongo.repository.TeamMgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mongo")
@RequiredArgsConstructor
public class MongoController {
    private final TeamMgRepository teamMgRepository;

    private final PlayerRepository playerRepository;

    @PostMapping("/teams")
    public ResponseEntity<TeamMg> createTeam(@RequestBody CreateTeamDto createTeamDto) {
        TeamMg teamMgCreated = teamMgRepository.save(createTeamDto.toTeam());

        return new ResponseEntity<>(teamMgCreated, HttpStatus.CREATED);
    }

    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@RequestBody CreatePlayerDto createPlayerDto) {
        Player playerCreated = playerRepository.save(createPlayerDto.toPlayer());

        return new ResponseEntity<>(playerCreated, HttpStatus.CREATED);
    }

    @PutMapping("/teams/{id}")
    public ResponseEntity<TeamMg> updateTeam(@PathVariable String id, @RequestBody CreateTeamDto createTeamDto) {
        Optional<TeamMg> optionalTeam = teamMgRepository.findById(id);

        if (optionalTeam.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        TeamMg teamMgToUpdate = optionalTeam.get()
                .setAddress(createTeamDto.getAddress())
                .setName(createTeamDto.getName())
                .setAcronym(createTeamDto.getAcronym());

        TeamMg teamMgUpdated = teamMgRepository.save(teamMgToUpdate);

        return new ResponseEntity<>(teamMgUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable String id, @RequestBody CreateTeamDto createTeamDto) {
        teamMgRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/players/bulk")
    public ResponseEntity<List<Player>> createPlayers(@RequestBody List<CreatePlayerDto> createPlayerDtoList) {
        List<Player> players = createPlayerDtoList
                .stream()
                .map(CreatePlayerDto::toPlayer)
                .collect(Collectors.toList());

        List<Player> playersCreated = playerRepository.saveAll(players);

        return new ResponseEntity<>(playersCreated, HttpStatus.CREATED);
    }

    @PostMapping("/teams/{id}/players")
    public ResponseEntity<TeamMg> addPlayersToTeam(@PathVariable String id, @RequestBody List<String> playerIds) {
        Optional<TeamMg> optionalTeam = teamMgRepository.findById(id);

        if (optionalTeam.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        TeamMg teamMgToUpdate = optionalTeam.get();

        Set<Player> playersToAdd = playerIds.stream()
                .map(playerId -> playerRepository.findById(playerId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        teamMgToUpdate.setPlayers(playersToAdd);

        TeamMg teamMgUpdated = teamMgRepository.save(teamMgToUpdate);

        return new ResponseEntity<>(teamMgUpdated, HttpStatus.OK);
    }

    @GetMapping("/teams")
    public ResponseEntity<List<TeamMg>> allTeams() {
        // List<Team> teams = teamRepository.findAll(Sort.by(Direction.DESC, "name"));
        List<TeamMg> teamMgs = teamMgRepository.findByIdIsNotNullOrderByNameDesc();

        return new ResponseEntity<>(teamMgs, HttpStatus.OK);
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> allPlayers() {
        List<Sort.Order> orders = new ArrayList<>() {{
            add(Sort.Order.by("position").with(Sort.Direction.ASC));
            add(Sort.Order.by("name").with(Sort.Direction.DESC));
        }};
        List<Player> players = playerRepository.findAll(Sort.by(orders));

        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamMg> oneTeam(@PathVariable String id) {
        Optional<TeamMg> teamOptional = teamMgRepository.findById(id);

        return teamOptional
                .map(teamMg -> new ResponseEntity<>(teamMg, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> onePlayer(@PathVariable String id) {
        Optional<Player> playerOptional = playerRepository.findById(id);

        return playerOptional
                .map(player -> new ResponseEntity<>(player, HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/players-example1")
    public ResponseEntity<List<Player>> listPlayersExample() {
        List<Player> players = playerRepository.findByPositionAndIsAvailable(PlayerPosition.STRIKER, true);

        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/teams-example2")
    public ResponseEntity<List<TeamMg>> listTeamsExample2() {
        List<TeamMg> teamMgs = teamMgRepository.findByNameContainingIgnoreCaseOrderByNameDesc("as");

        return new ResponseEntity<>(teamMgs, HttpStatus.OK);
    }

    @GetMapping("/players-example3")
    public ResponseEntity<List<Player>> listPlayersExample3() {
        List<PlayerPosition> playerPositions = new ArrayList<>() {{
            add(PlayerPosition.DEFENSIVE_MIDFIELDER);
            add(PlayerPosition.GOALKEEPER);
        }};

        List<Player> players = playerRepository.findDistinctNameByPositionIn(playerPositions);

        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/players-example4")
    public ResponseEntity<List<Player>> listPlayersExample4() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(1991, Calendar.JANUARY, 1);
        LocalDate fromDate = LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());

        calendar.set(1996, Calendar.JANUARY, 1);
        LocalDate toDate = LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());

        List<Player> players = playerRepository.findByBirthDateIsBetweenOrderByBirthDate(fromDate, toDate);

        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/players-example5")
    public ResponseEntity<Player> listPlayersExample5() {
        Player player = playerRepository.findFirstByOrderByBirthDateDesc();

        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping("/players-example6")
    public ResponseEntity<List<Player>> listPlayersExample6() {
        List<Player> players = playerRepository.findFirst10ByOrderByBirthDate();

        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/teams-example7/{city}")
    public ResponseEntity<List<TeamMg>> listTeamsExample7(@PathVariable String city) {
        List<TeamMg> teamMgs = teamMgRepository.findByAddressCityIgnoreCase(city);

        return new ResponseEntity<>(teamMgs, HttpStatus.OK);
    }

    @GetMapping("/players-page")
    public ResponseEntity<Page<Player>> listPlayersPage(@RequestParam int page) {
        Page<Player> players = playerRepository.findByIdIsNotNull(PageRequest.of(page - 1, 10));

        return new ResponseEntity<>(players, HttpStatus.OK);
    }
}
