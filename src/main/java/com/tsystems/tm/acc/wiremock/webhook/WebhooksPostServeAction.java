package com.tsystems.tm.acc.wiremock.webhook;

import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class WebhooksPostServeAction extends WebhookPostServeAction {
    public static final String NAME = "webhooks";

    @Override
    public String getName() {
        return NAME;
    }

    public static WebhooksPostServeActionDefinition webhooks() {
        return new WebhooksPostServeActionDefinition();
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {
        final WebhooksPostServeActionDefinition arrayDefinition = parameters.as(WebhooksPostServeActionDefinition.class);
        for (WebhookPostServeActionDefinition definition : arrayDefinition.getWebhooks()) {
            try {
                doActionInternal(definition, serveEvent, admin, parameters);
            } catch (Throwable e) {
                e.printStackTrace();
                throwUnchecked(e);
            }
        }
    }
}
