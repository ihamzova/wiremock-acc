package com.tsystems.tm.acc.wiremock.groovy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class GroovyPostServeActionDefinition {
    private String inline;
    private Map<String, Object> arguments;
    private String scriptFileName;

    @JsonCreator
    public GroovyPostServeActionDefinition(@JsonProperty("inline") String inline,
                                           @JsonProperty("arguments") Map<String, Object> arguments,
                                           @JsonProperty("scriptFileName") String scriptFileName) {
        this.inline = inline;
        if (arguments == null) {
            this.arguments = new HashMap<>();
        } else {
            this.arguments = arguments;
        }
        this.scriptFileName = scriptFileName;
    }

    public GroovyPostServeActionDefinition() {
        this.arguments = new HashMap<>();
    }

    public String getInline() {
        return inline;
    }

    public GroovyPostServeActionDefinition withInline(String inline) {
        this.inline = inline;
        return this;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public GroovyPostServeActionDefinition withArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
        return this;
    }

    public String getScriptFileName() {
        return scriptFileName;
    }

    public GroovyPostServeActionDefinition withScriptFileName(String scriptFileName) {
        this.scriptFileName = scriptFileName;
        return this;
    }
}
