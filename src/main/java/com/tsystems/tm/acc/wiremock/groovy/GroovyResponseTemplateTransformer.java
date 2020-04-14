package com.tsystems.tm.acc.wiremock.groovy;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;

public class GroovyResponseTemplateTransformer extends ResponseTemplateTransformer {

    public GroovyResponseTemplateTransformer() {
        super(true, GroovyHandlebarsHelper.NAME, new GroovyHandlebarsHelper());
    }

    public String getName() {
        return "groovy";
    }
}
