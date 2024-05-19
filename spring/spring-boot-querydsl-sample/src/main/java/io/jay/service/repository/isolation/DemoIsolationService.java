package io.jay.service.repository.isolation;

import com.github.javafaker.Faker;
import io.jay.service.config.ApplicationContextProvider;
import io.jay.service.entity.nonlock.TeamNoLock;
import io.jay.service.repository.jpa.TeamNoLockRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoIsolationService {

    private final TeamNoLockRepository teamJPARepository;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final PlatformTransactionManager transactionManager;

    private final EntityManager entityManager;
    private TransactionTemplate transactionTemplate;
    private final Semaphore semaphore = new Semaphore(1);
    private static final String DIRTY_READ = "DIRTY_READ";
    private static final String REPEATABLE_READ = "REPEATABLE_READ";
    private static final String PHANTOM_READ = "PHANTOM_READ";
    private ConcurrentHashMap<String, Boolean> results = new ConcurrentHashMap<>() {
        {
            put(DIRTY_READ, false);
            put(REPEATABLE_READ, false);
            put(PHANTOM_READ, false);
        }
    };

    private String insertDuplicateName = Faker.instance().name().name();
    private String dirtyRead = Faker.instance().name().name();

    @PostConstruct
    public void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void demoProcess(int isolationLevel) {
        switch (isolationLevel) {
            case TransactionDefinition.ISOLATION_READ_UNCOMMITTED:
            case TransactionDefinition.ISOLATION_READ_COMMITTED:
            case TransactionDefinition.ISOLATION_REPEATABLE_READ:
            case TransactionDefinition.ISOLATION_SERIALIZABLE:
                try {
                    semaphore.acquire();
                    this.dirtyRead = Faker.instance().name().name();
                    log.info("Check dirty read");
                    demoIsolation(isolationLevel, "checkDirtyRead");
                    semaphore.acquire();
                    log.info("Check repeatable read");
                    demoIsolation(isolationLevel, "checkRepeatableRead");
                    semaphore.acquire();
                    log.info("Check phantom read");
                    demoIsolation(isolationLevel, "checkPhantomRead");
                    semaphore.acquire();
                    log.info("Check Concurrent Insert");
                    this.insertDuplicateName = Faker.instance().name().name();
                    demoIsolation(isolationLevel, "checkConcurrentInsert");
                    semaphore.acquire();
                    log.info("Check Concurrent Update");
                    demoIsolation(isolationLevel, "updateConcurrentData");
                    semaphore.acquire();
                    log.info("Result : [{}]", results);
                    semaphore.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    public void demoIsolation(int isolationLevel, String methodName) throws InterruptedException {
        Phaser phaser = new Phaser(2);
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            transactionTemplate.setIsolationLevel(isolationLevel);
            transactionTemplate.execute((action) -> {
                try {
                    Object svc = ApplicationContextProvider.getContext().getBean(DemoIsolationService.class);
                    svc.getClass().getDeclaredMethod(methodName, Phaser.class, int.class).invoke(svc, phaser, 1);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
            log.info("Thread [{}] committed, finished phase [{}]", 1, phaser.getPhase());
            phaser.arriveAndDeregister(); // phase1
            return 42;
        }, executorService).orTimeout(20, TimeUnit.SECONDS).exceptionally(ex -> {
            log.error("Thread [{}] Error : [{}]", 1, ex.getMessage());
            phaser.arriveAndDeregister();
            return null;
        });
        CompletableFuture future2 = CompletableFuture.supplyAsync(() -> {
            transactionTemplate.setIsolationLevel(isolationLevel);
            transactionTemplate.execute((action) -> {
                try {
                    Object svc = ApplicationContextProvider.getContext().getBean(DemoIsolationService.class);
                    svc.getClass().getDeclaredMethod(methodName, Phaser.class, int.class).invoke(svc, phaser, 2);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
            log.info("Thread [{}] committed, finished phase [{}]", 2, phaser.getPhase());
            phaser.arriveAndDeregister();
            return 42;
        }, executorService).orTimeout(20, TimeUnit.SECONDS).exceptionally(ex -> {
            log.error("Thread [{}] Error : [{}]", 2, ex.getMessage());
            phaser.arriveAndDeregister();
            return null;
        });
        CompletableFuture.allOf(future, future2).thenAccept((results) -> {
            phaser.arriveAndDeregister();
            semaphore.release();
        });
    }

    public void demoIsolation(int isolationLevel) {
        CountDownLatch latch = new CountDownLatch(1);
        Phaser phaser = new Phaser(2);
        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            log.info("Thread [{}] started at Phase [{}]", 1, phaser.getPhase());
            transactionTemplate.setIsolationLevel(isolationLevel);
            transactionTemplate.execute((action) -> {
                try {
                    ApplicationContextProvider.getContext().getBean(
                            DemoIsolationService.class).processEntityTransaction1(latch, phaser, 1);
                } catch (InterruptedException | CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
            log.info("Thread [{}] committed, finished phase [{}]", 1, phaser.getPhase());
            phaser.arriveAndAwaitAdvance(); // phase1
            return 42;
        }, executorService).exceptionally(ex -> {
            log.error("Error : [{}]", ex.getMessage());
            phaser.arriveAndDeregister();
            return null;
        });
        CompletableFuture future2 = CompletableFuture.supplyAsync(() -> {
            log.info("Thread [{}] started at Phase [{}]", 2, phaser.getPhase());
            transactionTemplate.setIsolationLevel(isolationLevel);
            transactionTemplate.execute((action) -> {
                try {
                    ApplicationContextProvider.getContext().getBean(
                            DemoIsolationService.class).processEntityTransaction2(latch, phaser, 2);
                    log.info("Thread [{}] committed, finished phase [{}]", 2, phaser.getPhase());
                } catch (InterruptedException | CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });

            return 42;
        }, executorService).exceptionally(ex -> {
            log.error("Error : [{}]", ex.getMessage());
            phaser.arriveAndDeregister();
            return null;
        });
        CompletableFuture.allOf(future, future2).thenAccept((results) -> {
            log.info("Phaser unregister");
            phaser.arriveAndDeregister();
        });
    }

    //    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void processEntityTransaction1(CountDownLatch latch, Phaser phaser, int index) throws InterruptedException,
            CloneNotSupportedException {
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        String isolationName = getCurrentIsolation();
        log.info("Transaction Name [{}], Isolation [{}]", transactionName, isolationName);
        if (index != 1) {
            latch.await();
        }
        TeamNoLock team = teamJPARepository.findById((long) (1)).get();
        log.info(" Thread: [{}], Get team [{}]", index, team.getId());

//            teamJPARepository.updateTeamJPA(team.getId(), team.getName() + index);
        // Insert new record and check phantom read
        TeamNoLock team2 = (TeamNoLock) team.clone();
        team2.setId(null);
        teamJPARepository.save(team2);

        log.info("Thread [{}], COUNT teams=[{}] ,Check Phantom-Read, Isolation []", index,
                teamJPARepository.count(), isolationName);

        log.info("Thread [1], waiting thread [2] get and change data processing, finished phase [{}]",
                phaser.getPhase());
        latch.countDown();
        phaser.arriveAndAwaitAdvance(); // phase 0

        // Update data what thread 2 read before to check repeatable read
        team.setName(team.getName() + index);
        teamJPARepository.save(team);
        log.info("Thread [{}] UPDATE team [{}] - [{}]", index, team.getId(), team.getName());
    }

    //    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void processEntityTransaction2(CountDownLatch latch, Phaser phaser, int index) throws InterruptedException,
            CloneNotSupportedException {
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        String isolationName = getCurrentIsolation();
        log.info("Transaction Name [{}], Isolation [{}]", transactionName, isolationName);
        if (index != 1) {
            latch.await();
            log.info("Thread [{}] start processing", index);
        }
        TeamNoLock teamFirst = teamJPARepository.findById(1L).get();
        log.info("Thread: [{}], Get team: [{}] - [{}] first time", index, teamFirst.getId(), teamFirst.getName());

        // Check concurrent update
        teamFirst.setName(teamFirst.getName() + index);
        teamJPARepository.save(teamFirst);
        log.info("Thread: [{}], UPDATE data to check concurrent update, finished phase {}", index, phaser.getPhase());
        phaser.arriveAndAwaitAdvance(); // phase 0

        log.info("Thread [{}], will await thread 1 committed, finished phase [{}]", index, phaser.getPhase());
        phaser.arriveAndAwaitAdvance(); // phase 1

        // check phantom read
        TeamNoLock team2 = (TeamNoLock) teamFirst.clone();
        team2.setId(null);
        teamJPARepository.save(team2);

        log.info("Thread [{}], INSERTTed data", index, phaser.getPhase());
        log.info("Thread [{}], COUNT team=[{}] ,Check Phantom-Read?, Isolation []", index,
                teamJPARepository.count(), isolationName);

        TeamNoLock teamSecond = teamJPARepository.findById(1L).get();
        log.info("Thread: [{}], Get team: [{}] - [{}] second time", index, teamSecond.getId(), teamSecond.getName());
    }

    public void checkDirtyRead(Phaser phaser, int index) {
        String isolationName = getCurrentIsolation();
        log.info("Start Transaction [{}], Isolation [{}]", index, isolationName);
        phaser.arriveAndAwaitAdvance(); // 0
        // Check concurrent update
        if (index == 1) { // run first
            TeamNoLock teamFirst = teamJPARepository.findById(1L).get();
            teamFirst.setName("First team-" + dirtyRead);
//            teamJPARepository.save(teamFirst); // It will only call when end transaction, so can't test uncommitted data
            teamJPARepository.saveAndFlush(teamFirst);
            log.info("Thread: [{}], UPDATE DATA [{}], finished phase {}", index, teamFirst.getName(),
                    phaser.getPhase());
            phaser.arriveAndAwaitAdvance(); // 1
            phaser.arriveAndAwaitAdvance(); // 2
        } else {
            phaser.arriveAndAwaitAdvance(); // 1
//            log.info("Thread: [{}], Call clear entityManager to get new data", index);
//            entityManager.clear(); // Call clear to clear cache for entities loaded
            TeamNoLock teamBeforeCommit = teamJPARepository.findById(1L).get();
            log.info("Thread: [{}], UNCOMMITTED DATA, TEAM_NAME [{}]", index, teamBeforeCommit.getName());
            phaser.arriveAndAwaitAdvance(); // 2
            phaser.arriveAndAwaitAdvance(); // 3
//            TeamNoLock teamSecond = teamJPARepository.findById(1L).get();
//            log.info("Thread: [{}], GET COMMITTED DATA, TEAM_NAME [{}]", index, teamSecond.getName());
            boolean isDirtyRead = StringUtils.equals("First team-" + dirtyRead, teamBeforeCommit.getName());
            log.info("Before: [{}] {} [{}] : DirtyRead occurred: {}", teamBeforeCommit.getName(),
                    isDirtyRead ? "==" : "!=", "First team-" + dirtyRead, isDirtyRead);
            results.put(DIRTY_READ, isDirtyRead);
        }
    }

    public void checkRepeatableRead(Phaser phaser, int index) throws CloneNotSupportedException {
        String isolationName = getCurrentIsolation();
        log.info("Started Transaction [{}], Isolation [{}]", index, isolationName);
        phaser.arriveAndAwaitAdvance(); // 0
        // Check concurrent update
        if (index == 1) { // run first
            phaser.arriveAndAwaitAdvance(); // 1
            TeamNoLock team = teamJPARepository.findById(2L).get();
            team.setName(team.getName() + index);
            teamJPARepository.save(team); // It will only call when end transaction, so can't test uncommitted data
//            teamJPARepository.saveAndFlush(teamFirst);
            log.info("Thread: [{}], UPDATE data [{}], finished phase {}", index, team.getName(), phaser.getPhase());
            phaser.arriveAndAwaitAdvance(); // 2
        } else {
            TeamNoLock teamFirst = teamJPARepository.findById(2L).get();
            log.info("Thread: [{}], GET DATA, TEAM_NAME= [{}]", index, teamFirst.getName());
            phaser.arriveAndAwaitAdvance(); // 1
            phaser.arriveAndAwaitAdvance(); // 2
            phaser.arriveAndAwaitAdvance(); // 3
            log.info("Thread: [{}], Call clear entityManager to get new data", index);
            entityManager.clear(); // Call clear to clear cache for entities loaded
            TeamNoLock teamSecond = teamJPARepository.findById(2L).get();
            log.info("Thread: [{}], GET DATA second time, TEAM_NAME [{}]", index, teamSecond.getName());
            boolean isRepeatableRead = !StringUtils.equals(teamFirst.getName(), teamSecond.getName());
            log.info("Before: [{}] {} [{}] : RepeatableRead occurred: {}", teamFirst.getName(),
                    isRepeatableRead ? "!=" : "==", teamSecond.getName(), isRepeatableRead);
            results.put(REPEATABLE_READ, isRepeatableRead);
        }
    }

    public void checkPhantomRead(Phaser phaser, int index) throws CloneNotSupportedException {
        String isolationName = getCurrentIsolation();
        log.info("Transaction [{}], Isolation [{}]", index, isolationName);
        phaser.arriveAndAwaitAdvance(); // 0
        // Check concurrent update
        if (index == 1) { // run first
            phaser.arriveAndAwaitAdvance(); // 1
            TeamNoLock team = teamJPARepository.findById(1L).get();
            TeamNoLock teamClone = team.clone();
            teamClone.setName(team.getName() + index);
            teamJPARepository.save(teamClone); // It will only call when end transaction, so can't test uncommitted data
//            teamJPARepository.saveAndFlush(teamFirst);
            log.info("Thread: [{}], Insert data [{}], finished phase {}", index, team.getName(), phaser.getPhase());
            phaser.arriveAndAwaitAdvance(); // 2
        } else {
            long countBefore = teamJPARepository.count();
            log.info("Thread: [{}], COUNT TEAM [{}] Before commit", index, countBefore);
            phaser.arriveAndAwaitAdvance(); // 1
            phaser.arriveAndAwaitAdvance(); // 2
            phaser.arriveAndAwaitAdvance(); // 3
            log.info("Thread: [{}], Call clear entityManager to get new data", index);
            entityManager.clear(); // Call clear to clear cache for entities loaded
            long countAfter = teamJPARepository.count();
            log.info("Thread: [{}], COUNT TEAM [{}] After Thread 1 committed", index, countAfter);
            boolean isPhantomRead = countBefore != countAfter;
            log.info("Before: [{}] {} [{}] : PhantomRead occurred: {}", countBefore, isPhantomRead ? "!=" : "==",
                    countAfter, isPhantomRead);
            results.put(PHANTOM_READ, isPhantomRead);

        }
    }

    public void updateConcurrentData(Phaser phaser, int index) {
        String isolationName = getCurrentIsolation();
        log.info("Transaction [{}], Isolation [{}]", index, isolationName);
        phaser.arriveAndAwaitAdvance(); // 0
        // Check concurrent update
        if (index == 1) { // run first
            // Check concurrent update
            TeamNoLock teamFirst = teamJPARepository.findById(2L).get();
            teamFirst.setName(teamFirst.getName() + Thread.currentThread().getName());
//            teamJPARepository.save(teamFirst);
            teamJPARepository.saveAndFlush(teamFirst);
            log.info("Thread [{}], UPDATE data, finished phase {}",
                    index, phaser.getPhase());
            phaser.arriveAndAwaitAdvance(); // 1
            phaser.arriveAndAwaitAdvance(); // 2
        } else {
            phaser.arriveAndAwaitAdvance(); // 1
            // Check concurrent update
            TeamNoLock teamFirst = teamJPARepository.findById(2L).get();
            teamFirst.setName(teamFirst.getName() + Thread.currentThread().getName());
//            teamJPARepository.save(teamFirst);
            teamJPARepository.saveAndFlush(teamFirst);
            log.info("Thread: [{}], UPDATE data, finished phase {}",
                    index, phaser.getPhase());
            phaser.arriveAndAwaitAdvance(); // 2
        }
    }

    public void checkConcurrentInsert(Phaser phaser, int index) {
        String isolationName = getCurrentIsolation();
        log.info("Transaction [{}], Isolation [{}]", index, isolationName);
        phaser.arriveAndAwaitAdvance(); // 0
        // Check concurrent insert
        if (index == 1) { // run first
            // Check concurrent insert
            int duplicateTeam = teamJPARepository.findAllByName(insertDuplicateName).size();
            log.info("Thread [{}], Find duplicate data Count: {}",
                    index, duplicateTeam);
            TeamNoLock teamFirst = new TeamNoLock(insertDuplicateName);
            teamJPARepository.saveAndFlush(teamFirst);
            log.info("Thread [{}], INSERT data, finished phase {}",
                    index, phaser.getPhase());

            phaser.arriveAndAwaitAdvance(); // 1
            phaser.arriveAndAwaitAdvance(); // 2
        } else {
            phaser.arriveAndAwaitAdvance(); // 1
            // Check concurrent insert
            int duplicateTeam = teamJPARepository.findAllByName(insertDuplicateName).size();
            log.info("Thread [{}], Find duplicate data Count: {}",
                    index, duplicateTeam);
            TeamNoLock teamFirst = new TeamNoLock(insertDuplicateName);
            teamJPARepository.saveAndFlush(teamFirst);
            log.info("Thread: [{}], UPDATE data, finished phase {}",
                    index, phaser.getPhase());

            phaser.arriveAndAwaitAdvance(); // 2
        }
    }

    public String getCurrentIsolation() {
        int isolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
        switch (isolationLevel) {
            case TransactionDefinition.ISOLATION_READ_UNCOMMITTED:
                return Isolation.READ_UNCOMMITTED.name();
            case TransactionDefinition.ISOLATION_READ_COMMITTED:
                return Isolation.READ_COMMITTED.name();
            case TransactionDefinition.ISOLATION_REPEATABLE_READ:
                return Isolation.REPEATABLE_READ.name();
            case TransactionDefinition.ISOLATION_SERIALIZABLE:
                return Isolation.SERIALIZABLE.name();
            default:
                throw new IllegalArgumentException("Invalid isolation level: " + isolationLevel);
        }
    }
}
