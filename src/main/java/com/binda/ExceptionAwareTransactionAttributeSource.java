/*
 * Copyright (c) 2017, Sabre Holdings. All Rights Reserved.
 */

package com.binda;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.interceptor.DelegatingTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import java.lang.reflect.Method;

public class ExceptionAwareTransactionAttributeSource implements TransactionAttributeSource {

    private static final ThreadLocal<Throwable> ROLLBACK_EXCEPTION = new ThreadLocal<>();

    private static final Exception NO_EXCEPTION_WAS_STORED = new RuntimeException("No exception was stored");

    private final TransactionAttributeSource original;

    public ExceptionAwareTransactionAttributeSource(TransactionAttributeSource original) {
        this.original = original;
    }

    @Nullable
    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        final TransactionAttribute transactionAttribute = original.getTransactionAttribute(method, targetClass);
        return transactionAttribute == null ? null : new ExceptionAwareTransactionAttribute(transactionAttribute);
    }

    public static Throwable exceptionThatCausedLastRollback() {
        Throwable throwable = ROLLBACK_EXCEPTION.get();
        if (throwable != null) {
            return throwable;
        }

        return NO_EXCEPTION_WAS_STORED;
    }

    public static void makeRollbackCauseAvailableToCurrentThread(@NotNull Throwable rollbackCause) {
        ROLLBACK_EXCEPTION.set(rollbackCause);
    }

    private static final class ExceptionAwareTransactionAttribute extends DelegatingTransactionAttribute {
        private static final long serialVersionUID = -1047450469627950746L;

        /**
         * Create a DelegatingTransactionAttribute for the given target attribute.
         *
         * @param targetAttribute the target TransactionAttribute to delegate to
         */
        private ExceptionAwareTransactionAttribute(TransactionAttribute targetAttribute) {
            super(targetAttribute);
        }

        @Override
        public boolean rollbackOn(Throwable ex) {
            ROLLBACK_EXCEPTION.remove();
            final boolean result = super.rollbackOn(ex);
            if (result) {
                ROLLBACK_EXCEPTION.set(ex);
            }
            return result;
        }
    }

}
