package com.tsystems.tm.acc.wiremock.persist;

public interface PersistenceProvider {
    Object get(String key);
    void put(String key, Object value);
    Object clear(String key);
    void clear();
}
