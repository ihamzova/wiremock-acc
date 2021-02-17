package com.tsystems.tm.acc.wiremock.groovy;

import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class GroovyArrayPostServeAction extends GroovyPostServeAction {
    public static final String NAME = "groovyArray";

    public static GroovyArrayPostServeActionDefinition groovyArray() {
        return new GroovyArrayPostServeActionDefinition();
    }

    public String getName() {
        return NAME;
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {
        try {
            final GroovyArrayPostServeActionDefinition arrayDefinition = parameters.as(GroovyArrayPostServeActionDefinition.class);
            for (GroovyPostServeActionDefinition definition : arrayDefinition.getContent()) {
                doActionInternal(definition, serveEvent, admin, parameters);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
