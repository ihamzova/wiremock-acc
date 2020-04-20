package com.tsystems.tm.acc;

import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class WebhooksArray extends Webhooks {
    public static final String NAME = "webhooks";

    @Override
    public String getName() {
        return NAME;
    }

    public static WebhookArrayDefinition webhooks() {
        return new WebhookArrayDefinition();
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {
        final WebhookArrayDefinition arrayDefinition = parameters.as(WebhookArrayDefinition.class);
        for (WebhookDefinition definition : arrayDefinition.getWebhooks()) {
            try {
                doActionInternal(definition, serveEvent, admin, parameters);
            } catch (Throwable e) {
                e.printStackTrace();
                throwUnchecked(e);
            }
        }
    }
}
