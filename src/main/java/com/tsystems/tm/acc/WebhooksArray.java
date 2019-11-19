package com.tsystems.tm.acc;

import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

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
            doActionInternal(definition, serveEvent, admin, parameters);
        }
    }
}
