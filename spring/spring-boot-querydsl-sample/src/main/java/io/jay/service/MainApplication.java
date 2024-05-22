package io.jay.service;

import io.jay.service.entity.Member;
import io.jay.service.entity.Milestone;
import io.jay.service.entity.Team;
import io.jay.service.entity.jpa.TeamJPA;
import io.jay.service.model.MemberVI;
import io.jay.service.model.TeamVI;
import io.jay.service.model.projection.TeamDTO;
import io.jay.service.model.projection.TeamView;
import io.jay.service.repository.TeamRepository;
import io.jay.service.repository.blaze_persistence.BlazeService;
import io.jay.service.repository.isolation.DemoIsolationService;
import io.jay.service.repository.jpa.TeamJPARepository;
import io.jay.service.repository.jpa.hibernate.TeamHibernateImpl;
import io.jay.service.repository.lock.DemoLockService;
import io.jay.service.repository.querydsl.TeamQueryRepository;
import io.jay.service.repository.specification.TeamSpecification;
import io.jay.service.repository.specification.TeamSpecificationImpl;
import io.jay.service.repository.specification.TeamSpecificationRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}

@Component
@RequiredArgsConstructor
class DataInitializer implements CommandLineRunner {

    private final TeamRepository repository;

    @Override
    @Transactional
    public void run(String... args) {
        Team firstTeam = new Team("First Team");
        firstTeam.addMember(new Member("Jay"));
        firstTeam.addMember(new Member("Steve"));
        firstTeam.addMember(new Member("Jun"));
        firstTeam.addMember(new Member("Joel"));

        firstTeam.addMilestone(new Milestone("v1"));
        firstTeam.addMilestone(new Milestone("v2"));

        Team secondTeam = new Team("Second Team");
        secondTeam.addMember(new Member("Ats"));
        secondTeam.addMember(new Member("Ken"));
        secondTeam.addMember(new Member("Yu"));

        secondTeam.addMilestone(new Milestone("v1.1"));
        secondTeam.addMilestone(new Milestone("v2.1"));

        repository.saveAll(List.of(firstTeam, secondTeam));
    }
}

@RestController
@RequiredArgsConstructor
class TeamController {

    private final TeamQueryRepository query;

    private final TeamJPARepository teamJPARepository;

    @GetMapping("/v1/teams/{teamId}/members")
    public List<MemberVI.MemberResponse> members(@PathVariable long teamId) {
        return query.findMembersByTeamId(teamId);
    }

    @GetMapping("/v2/teams/{teamId}/members")
    public List<MemberVI.MemberResponse> searchMembers(@PathVariable long teamId,
                                                       @RequestParam(required = false) String searchText) {
        return query.searchMembersByTeamId(teamId, searchText);
    }

    @GetMapping("/v2/members")
    public List<MemberVI.MemberTeamResponse> searchMembers() {
        return query.findMembers();
    }

    @GetMapping("/v2/teams")
    public List<TeamView> searchTeams() {
        return teamJPARepository.findAllUsingJoinFetch();
    }

    @GetMapping("/v3/teams")
    public List<TeamVI.TeamMemberResponse> search3Teams() {
        return query.findTeams();
    }

    @GetMapping("/v3/members")
    public Page<MemberVI.MemberResponse> paginatedMembers(Pageable pageable) {
        return query.members(pageable);
    }

}

@Controller
@RequestMapping("/dsl")
@ResponseBody
@RequiredArgsConstructor
class QueryDslController {

    private final TeamQueryRepository query;

    @GetMapping("/teams/{id}")
    public List<TeamVI.TeamMemberResponse> search3Teams(@PathVariable long id,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String member) {
        return query.findTeamByIDAndName(id, name, member);
    }

    @GetMapping("/teams/v2/{id}")
    public List<TeamVI.TeamMemberResponse> searchTeamsV2(@PathVariable long id,
                                                         @RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String member) {
        return query.findTeamByIDAndNameProjection(id, name, member);
    }

    @GetMapping("/teams/v3/{id}")
    public List<TeamVI.TeamMemberResponse> searchTeamsV3(@PathVariable long id,
                                                         @RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String member) {
        return query.findTeamByIDAndNameSubQuery(id, name, member);
    }
}

@Controller
@RequestMapping("/criteria")
@ResponseBody
@RequiredArgsConstructor
class CriteriaController {

    private final TeamRepository teamRepository;

    @GetMapping("/teams/{id}")
    public List<TeamDTO> search3Teams(@PathVariable long id, @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String member) {
        return teamRepository.retrieveTeamsByCriteria(id, name, member);
    }

    @GetMapping("/teams/v2/{id}")
    public List<TeamDTO> searchTeamsV2(@PathVariable long id, @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String member) {
        return teamRepository.retrieveTeamsByCriteriaProjection(id, name, member);
    }
}

@Controller
@RequestMapping("/blaze")
@ResponseBody
@RequiredArgsConstructor
class BlazeController {

    private final BlazeService blazeService;

    @GetMapping("/teams/{id}")
    public List<io.jay.service.repository.blaze_persistence.TeamView> search3Teams(@PathVariable long id,
                                                                                   @RequestParam(required = false) String name,
                                                                                   @RequestParam(required = false) String member) {
        return blazeService.retrieveTeamsByCriteria(id, name, member);
    }

    @GetMapping("/teams/v2/{id}")
    public List<io.jay.service.repository.blaze_persistence.TeamView> searchTeamsV2(@PathVariable long id,
                                                                                    @RequestParam(required = false) String name,
                                                                                    @RequestParam(required = false) String member) {
        return blazeService.retrieveTeamsByFetchAlias(id, name, member);
    }

    @GetMapping("/teams/v3/{id}")
    public List<io.jay.service.repository.blaze_persistence.TeamView.JoinTeamView> searchTeamsV3(@PathVariable long id,
                                                                                                 @RequestParam(required = false) String name,
                                                                                                 @RequestParam(required = false) String member) {
        return blazeService.retrieveTeamsByEntityView(id, name, member);
    }
}

@Controller
@RequestMapping("/hib")
@ResponseBody
@RequiredArgsConstructor
class HibernateController {

    private final TeamHibernateImpl teamHibernate;

    @GetMapping("/teams")
    public List<TeamView.TeamSubView> searchAllTeams() {
        return teamHibernate.findAllDefault();
    }

    @GetMapping("/teams/{id}")
    public List<TeamView> searchTeams(@PathVariable long id, @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String member) {
        return teamHibernate.findTeamByIDAndName(id, name, member);
    }

    @GetMapping("/teams/v2/{id}")
    public List<TeamDTO> searchTeamsV2(@PathVariable long id, @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String member) {
        return teamHibernate.findTeamByIDAndNameProjection(id, name, member);
    }

    @GetMapping("/teams/v3/{id}")
    public List<TeamView> searchTeamsV3(@PathVariable long id, @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String member) {
        return teamHibernate.findTeamByIDAndNameInterface(id, name, member);
    }
}

@Controller
@RequestMapping("/spec")
@ResponseBody
@RequiredArgsConstructor
class SpecificationController {

    private final TeamSpecificationRepository teamSpecificationRepository;

    private final TeamSpecificationImpl teamSpecification;

    @GetMapping("/teams")
    public List<TeamView> search3Teams() {
        return teamSpecificationRepository.findBy(TeamSpecification.withMember(null),
                t -> t.as(TeamView.class).all());
    }

    @GetMapping("/teams/{id}")
    public List<TeamJPA> search3Teams(@PathVariable long id, @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String member) {
        return teamSpecification.findTeamByIDAndName(id, name, member);
    }

    @GetMapping("/teams/v2/{id}")
    public List<TeamView> searchTeamsV2(@PathVariable long id, @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String member) {
        return teamSpecification.findTeamByIDAndNameProjection(id, name, member);
    }
}

@Controller
@RequestMapping("/lock")
@ResponseBody
@RequiredArgsConstructor
class LockController {

    private final DemoLockService service;

    private final DemoIsolationService demoIsolationService;

    /**
     * Postgresql/ Mysql : Select not block on other transaction (select ... for share)
     * Oracle: Select block on other transaction (select ... for update)
     * Mysql: error when save
     *
     * @return
     */
    @GetMapping("/pess-read")
    public ResponseEntity<?> search3Teams() {
        service.demoPessimisticLock(LockModeType.PESSIMISTIC_READ);
        return ResponseEntity.ok().build();
    }

    /**
     * Postgresql/ Mysql : Select block on other transaction (select ... for no key update)
     * Oracle: Select block on other transaction (select ... for update)
     *
     * @return
     */
    @GetMapping("/pess-write")
    public ResponseEntity<?> searchLockTeams() {
        service.demoPessimisticLock(LockModeType.PESSIMISTIC_WRITE);
        return ResponseEntity.ok().build();
    }

    /**
     * Postgresql : Select will update version record. Other transaction will error while get lock at the same time
     * (select ... for no key update nowait)
     * Oracle/ Mysql: Select will update version record. Other transaction will error while get lock at the same time
     * (select ... for update nowait)
     *
     * @return
     */
    @GetMapping("/pess-force")
    public ResponseEntity<?> searchPessForceTeams() {
        service.demoPessimisticLock(LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        return ResponseEntity.ok().build();
    }

    /**
     * Postgresql / Oracle / Mysql : Not block Other transaction select.
     * Will error while update the same row (must using saveAndFlush())
     * version will increment 1 time (not including failed turn)
     *
     * @return
     */
    @GetMapping("/op")
    public ResponseEntity<?> searchOpTeams() {
        service.demoOptimisticLock(LockModeType.OPTIMISTIC);
        return ResponseEntity.ok().build();
    }

    /**
     * Postgresql / Oracle/ Mysql : Not block Other transaction select.
     * Will error while update the same row (must using saveAndFlush())
     * version will increment 2 times (including failed turn)
     *
     * @return
     */
    @GetMapping("/op-force")
    public ResponseEntity<?> searchOpForceTeams() {
        service.demoOptimisticLock(LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        return ResponseEntity.ok().build();
    }

    /**
     * Postgresql :
     * 1 : Result : [{DIRTY_READ=false, PHANTOM_READ=true, REPEATABLE_READ=true}]
     * Check Concurrent Update [False], Check Concurrent Insert [True]
     * 2 : Result : [{DIRTY_READ=false, PHANTOM_READ=true, REPEATABLE_READ=true}]
     * Check Concurrent Update [False], Check Concurrent Insert [True]
     * 4 : Result : [{DIRTY_READ=false, PHANTOM_READ=false, REPEATABLE_READ=false}]
     * Check Concurrent Update [False], Check Concurrent Insert [True]
     * 8 : Result : [{DIRTY_READ=false, PHANTOM_READ=false, REPEATABLE_READ=false}]
     * Check Concurrent Update [False], Check Concurrent Insert [False]
     * <p>
     * Oracle :
     * 1 : Not supported
     * 2 : Result : [{DIRTY_READ=false, PHANTOM_READ=true, REPEATABLE_READ=true}]
     * Check Concurrent Update [False - Deadlock] , Check Concurrent Insert [True]
     * 4 : Not supported
     * 8 : Result : [{DIRTY_READ=false, PHANTOM_READ=false, REPEATABLE_READ=false}]
     * Check Concurrent Update [False - Deadlock], Check Concurrent Insert [True]
     * <p>
     * Mysql :
     * 1 : Result : [{DIRTY_READ=true, PHANTOM_READ=true, REPEATABLE_READ=true}]
     * Check Concurrent Update [False], Check Concurrent Insert [True]
     * 2 : Result : [{DIRTY_READ=false, PHANTOM_READ=true, REPEATABLE_READ=true}]
     * Check Concurrent Update [False], Check Concurrent Insert [True]
     * 4 : Result : [{DIRTY_READ=false, PHANTOM_READ=false, REPEATABLE_READ=false}]
     * Check Concurrent Update [False], Check Concurrent Insert [True]
     * 8 : Result : [{DIRTY_READ=false, PHANTOM_READ=false, REPEATABLE_READ=false}]
     * Check Concurrent Update [False], Check Concurrent Insert [False]
     *
     * @param level
     * @return
     */
    @GetMapping("/isolation/{level}")
    public ResponseEntity<?> updateTeamsBySer(@PathVariable int level) {
        demoIsolationService.demoProcess(level);
        return ResponseEntity.ok().build();
    }
}

@Controller
@ResponseBody
@RequiredArgsConstructor
class DashboardController {

    private final TeamRepository teamRepository;

    @GetMapping("/v1/dashboard")
    public List<TeamVI.TeamResponse> dashboard1() {
        List<Team> all = teamRepository.findAllUsingJoinFetch();
        return all.stream()
                .map(team -> new TeamVI.TeamResponse(team.getId(), team.getName(), team.getMembers().size(),
                        team.getMilestones().size()))
                .toList();
    }

    @GetMapping("/v2/dashboard")
    public List<TeamVI.TeamResponse> dashboard2() {
        List<Team> all = teamRepository.findAll();
        return all.stream()
                .map(team -> new TeamVI.TeamResponse(team.getId(), team.getName(), team.getMembers().size(),
                        team.getMilestones().size()))
                .toList();
    }

    @GetMapping("/v3/dashboard")
    public Page<TeamVI.TeamResponse> dashboard3(Pageable pageable) {
        Page<Team> all = teamRepository.findAll(pageable);
        var content = all.stream()
                .map(team -> new TeamVI.TeamResponse(team.getId(), team.getName(), team.getMembers().size(),
                        team.getMilestones().size()))
                .toList();
        return new PageImpl<>(content, pageable, all.getTotalElements());
    }
}