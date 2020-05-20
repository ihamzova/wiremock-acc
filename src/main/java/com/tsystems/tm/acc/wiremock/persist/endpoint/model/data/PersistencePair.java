package com.tsystems.tm.acc.wiremock.persist.endpoint.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "value" })
public class PersistencePair {
    private String name;
    private Object value;

    public PersistencePair(
            @JsonProperty("name") String name,
            @JsonProperty("value") Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
