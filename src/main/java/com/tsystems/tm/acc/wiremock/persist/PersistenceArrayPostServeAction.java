package com.tsystems.tm.acc.wiremock.persist;

import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class PersistenceArrayPostServeAction extends PersistencePostServeAction {
    public static final String NAME = "persistArray";

    public static PersistenceArrayPostServeActionDefinition persistenceArray() {
        return new PersistenceArrayPostServeActionDefinition();
    }

    public String getName() {
        return NAME;
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {
        try {
            final PersistenceArrayPostServeActionDefinition arrayDefinition = parameters.as(PersistenceArrayPostServeActionDefinition.class);
            for (PersistencePostServeActionDefinition definition : arrayDefinition.getContent()) {
                doActionInternal(definition, serveEvent, admin, parameters);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
