package com.tsystems.tm.acc.wiremock.persist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PersistenceArrayPostServeActionDefinition {
    private List<PersistencePostServeActionDefinition> content;

    @JsonCreator
    public PersistenceArrayPostServeActionDefinition(@JsonProperty("content") List<PersistencePostServeActionDefinition> content) {
        this.content = content;
    }

    public PersistenceArrayPostServeActionDefinition() {
        this.content = new ArrayList<>();
    }

    public List<PersistencePostServeActionDefinition> getContent() {
        return content;
    }

    public PersistenceArrayPostServeActionDefinition withContent(List<PersistencePostServeActionDefinition> content) {
        this.content = content;
        return this;
    }
}
