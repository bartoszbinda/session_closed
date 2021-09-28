/*
 * Copyright (c) 2020, Sabre Holdings. All Rights Reserved.
 */

package com.binda;

import com.google.common.base.Stopwatch;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.persistence.PersistenceException;
import java.util.concurrent.TimeUnit;

@Component
public class StatsCollectingTransactionManager extends HibernateTransactionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsCollectingTransactionManager.class);
    private static final long serialVersionUID = 6511344557852573953L;
    private TransactionsStatistics statistics = new TransactionsStatistics();
    NullTransactionalResourceManager transactionalResourceManager = new NullTransactionalResourceManager();
    private LocalSessionFactoryBean factoryBean;
    private SessionFactory sessionFactory;
    private boolean earlyFlushBeforeCommit;

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        super.setSessionFactory(sessionFactory);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        statistics.transactionBegan();
        super.doBegin(transaction, definition);
        transactionalResourceManager.transactionBegin();
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            super.doCommit(status);
            if (stopwatch.elapsed(TimeUnit.MILLISECONDS) > 100) {
                LOGGER.info("Committed in {}", stopwatch);
            }
            if (!status.isRollbackOnly()) {
                transactionalResourceManager.transactionCommitted();
            }
            statistics.transactionCommitted();
        } catch (Exception e) {
            ExceptionAwareTransactionAttributeSource.makeRollbackCauseAvailableToCurrentThread(e);
            statistics.transactionRolledBack();
            transactionalResourceManager.transactionRolledback();
            throw e;
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        try {
            super.doRollback(status);
        } catch (Exception e) {
            ExceptionAwareTransactionAttributeSource.makeRollbackCauseAvailableToCurrentThread(e);
        } finally {
            statistics.transactionRolledBack();
            transactionalResourceManager.transactionRolledback();

        }
    }

    @Override
    protected Object doSuspend(Object transaction) {
        Object suspendedResources = super.doSuspend(transaction);
        transactionalResourceManager.transactionSuspend(transaction, suspendedResources);
        return suspendedResources;
    }

    @Override
    protected void doResume(Object transaction, Object suspendedResources) {
        super.doResume(transaction, suspendedResources);
        transactionalResourceManager.transactionResume(transaction, suspendedResources);
    }

    @Override
    protected void prepareForCommit(DefaultTransactionStatus status) {
        if (status.isNewTransaction()) {
            if (earlyFlushBeforeCommit) {
                Session session = sessionFactory.getCurrentSession();
                if (!session.getHibernateFlushMode().lessThan(FlushMode.COMMIT)) {
                    try {
                        Stopwatch stopwatch = Stopwatch.createStarted();
                        int entityCount = session.getStatistics().getEntityCount();
                        if (entityCount > 2) {
                            LOGGER.info("Performing an early flush for Hibernate transaction");
                        }
                        session.flush();
                        session.clear();
                        if (entityCount > 2) {
                            LOGGER.info("Early flush (readonly = {}) performed in {} - count {}", status.isReadOnly(), stopwatch, entityCount);
                        }
                    } catch (HibernateException ex) {
                        ExceptionAwareTransactionAttributeSource.makeRollbackCauseAvailableToCurrentThread(ex);
                        throw factoryBean.translateExceptionIfPossible(ex);
                    } catch (PersistenceException ex) {
                        ExceptionAwareTransactionAttributeSource.makeRollbackCauseAvailableToCurrentThread(ex);
                        throw factoryBean.translateExceptionIfPossible(ex);
                    } finally {
                        session.setHibernateFlushMode(FlushMode.MANUAL);
                    }
                }
            }
        }
    }

    @Required
    public void setEarlyFlushBeforeCommit(boolean earlyFlushBeforeCommit) {
        this.earlyFlushBeforeCommit = earlyFlushBeforeCommit;
    }
    @Autowired
    public StatsCollectingTransactionManager(SessionFactory sessionFactory, NullTransactionalResourceManager transactionalResourceManager, LocalSessionFactoryBean factoryBean, SessionFactory sessionFactory1) {
        super(sessionFactory);
        this.transactionalResourceManager = transactionalResourceManager;
        this.factoryBean = factoryBean;
        this.sessionFactory = sessionFactory1;
    }

    public void cleanup() {
        statistics.clear();
        transactionalResourceManager = null;
        sessionFactory = null;
    }
}
