/*
 * Copyright (c) 2020, Sabre Holdings. All Rights Reserved.
 */

package com.binda;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@ManagedResource(description = "transaction statistics which helps track transactions and its origins")
public class TransactionsStatistics {
    private static final long serialVersionUID = -6830115012699851198L;

    private static final Logger logger = LoggerFactory.getLogger(TransactionsStatistics.class);

    private boolean captureStackTrace;
    private final AtomicInteger transactionsBegan = new AtomicInteger();
    private final AtomicInteger transactionsCommitted = new AtomicInteger();
    private final AtomicInteger transactionsRolledBack = new AtomicInteger();
    private final ConcurrentMap<StackTraceElementWrapper, AtomicInteger> transactionBeganStackTrace = new ConcurrentHashMap<>();

    @ManagedOperation
    public void setCaptureStackTrace(boolean captureStackTrace) {
        this.captureStackTrace = captureStackTrace;
    }

    @ManagedAttribute(description = "number of transactions began so far")
    public int getTransactionsBegan() {
        return transactionsBegan.get();
    }

    @ManagedAttribute(description = "number of transactions committed")
    public int getTransactionsCommitted() {
        return transactionsCommitted.get();
    }

    @ManagedAttribute(description = "number of transactions rolled back")
    public int getTransactionsRolledback() {
        return transactionsRolledBack.get();
    }

    public int transactionBegan() {
        int began = transactionsBegan.incrementAndGet();

        if (captureStackTrace) {
            updateTransactionsStackTrace();
        }

        return began;
    }

    public int transactionCommitted() {
        return transactionsCommitted.incrementAndGet();
    }

    public int transactionRolledBack() {
        return transactionsRolledBack.incrementAndGet();
    }

    @ManagedAttribute(description = "shows the origin of transaction and number of transactions")
    public Map<String, Integer> getTransactionBeganStackTrace() {
        Map<StackTraceElementWrapper, AtomicInteger> tmp = new HashMap<>(transactionBeganStackTrace);

        Map<String, Integer> result = new HashMap<>(tmp.size());
        for (Entry<StackTraceElementWrapper, AtomicInteger> entry : tmp.entrySet()) {
            StackTraceElementWrapper key = entry.getKey();
            StackTraceElement[] stackTrace = key.getStackTrace();
            StringBuilder builder = new StringBuilder();
            for (StackTraceElement trace : stackTrace) {
                builder.append(trace).append('\n');
            }

            result.put(builder.toString(), entry.getValue().get());
        }

        return result;
    }

    @ManagedOperation(description = "clear statistics")
    public void clear() {
        transactionsBegan.set(0);
        transactionsCommitted.set(0);
        transactionsRolledBack.set(0);
        transactionBeganStackTrace.clear();
    }

    @ManagedOperation(description = "write statistics to the log")
    public void writeToLog() {
        logger.info(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("transactionsBegan=").append(getTransactionsBegan())
                .append(", transactionsCommited=").append(getTransactionsCommitted())
                .append(", transactionsRolledBack=").append(getTransactionsRolledback());

        builder.append("\nTransactions stack trace:\n");
        for (Entry<String, Integer> entry : getTransactionBeganStackTrace().entrySet()) {
            builder.append(entry.getValue()).append(": ").append(entry.getKey());
        }

        return builder.toString();
    }


    private void updateTransactionsStackTrace() {
        StackTraceElementWrapper stackTrace = new StackTraceElementWrapper(new Exception().getStackTrace());
        AtomicInteger counter = transactionBeganStackTrace.putIfAbsent(stackTrace, new AtomicInteger(1));
        if (counter != null) {
            counter.incrementAndGet();
        }
    }

    private class StackTraceElementWrapper {
        @NotNull
        private StackTraceElement[] stackTrace;

        private StackTraceElementWrapper(@NotNull StackTraceElement[] stackTrace) {
            this.stackTrace = stackTrace;
        }

        @NotNull
        private StackTraceElement[] getStackTrace() {
            return stackTrace;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof StackTraceElementWrapper)) {
                return false;
            }

            StackTraceElementWrapper that = (StackTraceElementWrapper) obj;
            return Arrays.equals(getStackTrace(), that.getStackTrace());
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(getStackTrace());
        }
    }
}
