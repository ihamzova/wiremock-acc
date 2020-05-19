package com.tsystems.tm.acc.wiremock.persist.impl.memory;

import com.tsystems.tm.acc.wiremock.persist.PersistenceProvider;

import java.util.*;

public class MemoryPersistenceProvider implements PersistenceProvider {
    private static MemoryPersistenceProvider INSTANCE;
    private final Map<String, Object> storage = Collections.synchronizedMap(new HashMap<>());

    synchronized public static MemoryPersistenceProvider get() {
        if (INSTANCE == null) {
            INSTANCE = new MemoryPersistenceProvider();
        }
        return INSTANCE;
    }

    @Override
    public Object get(String key) {
        return storage.get(key);
    }

    @Override
    public Set<Map.Entry<String, Object>> getAll() {
        return storage.entrySet();
    }

    @Override
    public void put(String key, Object value) {
        storage.put(key, value);
    }

    @Override
    public Object clear(String key) {
        Object value = storage.get(key);
        storage.remove(key);
        return value;
    }

    @Override
    public void clear() {
        storage.clear();
    }
}
