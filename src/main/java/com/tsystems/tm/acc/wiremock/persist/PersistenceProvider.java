package com.tsystems.tm.acc.wiremock.persist;

import java.util.Map;
import java.util.Set;

public interface PersistenceProvider {
    Object get(String key);
    Set<Map.Entry<String, Object>> getAll();
    void put(String key, Object value);
    Object clear(String key);
    void clear();
}
