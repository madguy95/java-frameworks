package io.jay.service.repository.lock;

import io.jay.service.config.ApplicationContextProvider;
import io.jay.service.entity.jpa.TeamJPA;
import io.jay.service.repository.jpa.TeamJPARepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoLockService {

    private final TeamJPARepository teamJPARepository;
    private final Semaphore semaphore = new Semaphore(2);
    private final EntityManager entityManager;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Transactional
    public void demoPessimisticLock(LockModeType type) {
        CountDownLatch latch = new CountDownLatch(1);
        executorService.submit(() -> ApplicationContextProvider.getContext().getBean(DemoLockService.class)
                .getEntityWithPessimisticLock(type, latch, 1));
        executorService.submit(() -> ApplicationContextProvider.getContext().getBean(DemoLockService.class)
                .getEntityWithPessimisticLock(type, latch, 2));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public TeamJPA getEntityWithPessimisticLock(LockModeType type, CountDownLatch latch, int index) {
        try {
            String isolationName = getCurrentIsolation();
            log.info("Transaction [{}], Isolation [{}]", index, isolationName);
            log.info("Call Time {}", index);
            if (index != 1) {
                latch.await();
            }
            TeamJPA team = switch (type) {
                case PESSIMISTIC_READ -> teamJPARepository.findByIdWithPessimisticRead(1L).get();
                case PESSIMISTIC_WRITE -> teamJPARepository.findByIdWithPessimisticWrite(1L).get();
                case PESSIMISTIC_FORCE_INCREMENT -> teamJPARepository.findByIdWithPessimisticForce(1L).get();
                default -> throw new RuntimeException(String.format("Not supported {%s}", type));
            };
            log.info("Time[{}] - Get team {}", index, team);
            team.setName(team.getName() + index);
            teamJPARepository.save(team);
            latch.countDown();
            Thread.sleep(10000);
            log.info("Time[{}] - Released pessimistic lock on team {}", index, team.getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (PersistenceException e) {
            log.error("Error: ", e);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return null;
    }

    @Transactional
    public void demoOptimisticLock(LockModeType type) {
        CountDownLatch latch = new CountDownLatch(1);
        executorService.submit(() -> ApplicationContextProvider.getContext().getBean(DemoLockService.class)
                .getEntityWithOptimisticLock(type, latch, 1));
        executorService.submit(() -> ApplicationContextProvider.getContext().getBean(DemoLockService.class)
                .getEntityWithOptimisticLock(type, latch, 0));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public TeamJPA getEntityWithOptimisticLock(LockModeType type, CountDownLatch latch, int index) {
        try {
            log.info("Call Time {}", index);
            if (index != 1) {
                latch.await();
            }
            TeamJPA team = switch (type) {
                case OPTIMISTIC -> teamJPARepository.findByIdWithOptimistic(1L).get();
                case OPTIMISTIC_FORCE_INCREMENT -> teamJPARepository.findByIdWithOptimisticForce(1L).get();
                default -> throw new RuntimeException(String.format("Not supported {%s}", type));
            };
            log.info("Get team {} time: {}", team, index);
            team.setName(team.getName() + index);
            teamJPARepository.saveAndFlush(
                    team); // Call save and save flush will return different result (by sql update will run right away when flush)
            latch.countDown();
            Thread.sleep(10000);
            log.info("Released Optimistic lock on team {} time {}", team.getId(), index);
        } catch (InterruptedException e) {
            log.error("Error: {}  {}", e, e.getMessage());
            throw new RuntimeException(e);
        } catch (ObjectOptimisticLockingFailureException sose) {
            log.error("Error: {}  {}", sose, sose.getMessage());
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return null;
    }

    public String getCurrentIsolation() {
        AtomicInteger isolationLevel = new AtomicInteger(
                Optional.ofNullable(TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()).orElse(
                        -1));
        Session session = null;
        Transaction txn = null;
        if (isolationLevel.get() == -1) {
            try {
                session = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).openSession();
                txn = session.beginTransaction();
                session.doWork(new Work() {
                    @Override
                    public void execute(Connection connection) throws SQLException {
                        isolationLevel.set(connection.getTransactionIsolation());
                    }
                });
                txn.commit();
            } catch (RuntimeException e) {
                if (txn != null && txn.isActive()) txn.rollback();
                throw e;
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
        switch (isolationLevel.get()) {
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
