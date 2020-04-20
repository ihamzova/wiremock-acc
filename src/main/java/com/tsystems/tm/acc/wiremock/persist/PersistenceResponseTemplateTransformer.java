package com.tsystems.tm.acc.wiremock.persist;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;

public class PersistenceResponseTemplateTransformer extends ResponseTemplateTransformer {

    public PersistenceResponseTemplateTransformer() {
        super(true, PersistenceHandlebarsHelper.NAME, new PersistenceHandlebarsHelper());
    }

    public String getName() {
        return "persist";
    }
}
