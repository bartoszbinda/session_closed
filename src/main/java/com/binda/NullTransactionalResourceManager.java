package com.binda;

import org.springframework.stereotype.Component;

@Component
public class NullTransactionalResourceManager {
    public void transactionBegin() {
    }

    public void transactionCommitted() {
    }

    public void transactionRolledback() {
    }

    public void transactionSuspend(Object transaction, Object suspendedResources) {
    }

    public void transactionResume(Object transaction, Object suspendedResources) {
    }
}
