package com.tsystems.tm.acc.wiremock.persist;

import java.util.Map;
import java.util.Set;

public class PersistenceService implements PersistenceProvider {
    private static PersistenceService INSTANCE;
    private PersistenceProvider persistenceProvider;

    private PersistenceService() {
        this.persistenceProvider = new PersistenceProviderFactory().get();
    }

    synchronized public static PersistenceService get() {
        if (INSTANCE == null) {
            INSTANCE = new PersistenceService();
        }
        return INSTANCE;
    }

    @Override
    public Object get(String key) {
        return persistenceProvider.get(key);
    }

    @Override
    public Set<Map.Entry<String, Object>> getAll() {
        return persistenceProvider.getAll();
    }

    @Override
    public void put(String key, Object value) {
        persistenceProvider.put(key, value);
    }

    @Override
    public Object clear(String key) {
        return persistenceProvider.clear(key);
    }

    @Override
    public void clear() {
        persistenceProvider.clear();
    }
}
