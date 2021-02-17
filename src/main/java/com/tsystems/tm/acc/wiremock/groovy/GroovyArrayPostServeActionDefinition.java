package com.tsystems.tm.acc.wiremock.groovy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class GroovyArrayPostServeActionDefinition {
    private List<GroovyPostServeActionDefinition> content;

    @JsonCreator
    public GroovyArrayPostServeActionDefinition(@JsonProperty("content") List<GroovyPostServeActionDefinition> content) {
        this.content = content;
    }

    public GroovyArrayPostServeActionDefinition() {
        this.content = new ArrayList<>();
    }

    public List<GroovyPostServeActionDefinition> getContent() {
        return content;
    }

    public GroovyArrayPostServeActionDefinition withContent(List<GroovyPostServeActionDefinition> content) {
        this.content = content;
        return this;
    }
}
