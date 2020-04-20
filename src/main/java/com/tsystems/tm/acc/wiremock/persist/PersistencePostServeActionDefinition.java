package com.tsystems.tm.acc.wiremock.persist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PersistencePostServeActionDefinition {
    private ActionType actionType;
    private String key;
    private String value;
    private String fileName;
    private Integer fixedDelayMilliseconds;

    @JsonCreator
    public PersistencePostServeActionDefinition(@JsonProperty("action") ActionType actionType,
                                                @JsonProperty("key") String key,
                                                @JsonProperty("value") String value,
                                                @JsonProperty("fileName") String fileName,
                                                @JsonProperty("fixedDelayMilliseconds") Integer fixedDelayMilliseconds) {
        this.actionType = actionType;
        this.key = key;
        this.value = value;
        this.fileName = fileName;
        this.fixedDelayMilliseconds = fixedDelayMilliseconds;
    }

    public PersistencePostServeActionDefinition() {
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFileName() {
        return fileName;
    }

    public Integer getFixedDelayMilliseconds() {
        return fixedDelayMilliseconds;
    }

    public PersistencePostServeActionDefinition withActionType(ActionType actionType) {
        this.actionType = actionType;
        return this;
    }

    public PersistencePostServeActionDefinition withKey(String key) {
        this.key = key;
        return this;
    }

    public PersistencePostServeActionDefinition withValue(String value) {
        this.value = value;
        return this;
    }

    public PersistencePostServeActionDefinition withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public PersistencePostServeActionDefinition withFixedDelayMilliseconds(Integer fixedDelayMilliseconds) {
        this.fixedDelayMilliseconds = fixedDelayMilliseconds;
        return this;
    }
}
