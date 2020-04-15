package com.tsystems.tm.acc.wiremock.persist;

import com.tsystems.tm.acc.wiremock.persist.impl.memory.MemoryPersistenceProvider;

public class PersistenceProviderFactory {
    public PersistenceProvider get() {
        return MemoryPersistenceProvider.get();
    }
}
