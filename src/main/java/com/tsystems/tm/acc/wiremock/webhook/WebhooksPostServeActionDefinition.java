package com.tsystems.tm.acc.wiremock.webhook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class WebhooksPostServeActionDefinition {
    private List<WebhookPostServeActionDefinition> webhooks;

    @JsonCreator
    public WebhooksPostServeActionDefinition(@JsonProperty("webhooks") ArrayList<WebhookPostServeActionDefinition> webhooks) {
        this.webhooks = webhooks;
    }

    public WebhooksPostServeActionDefinition() {
        this.webhooks = new ArrayList<>();
    }

    public List<WebhookPostServeActionDefinition> getWebhooks() {
        return webhooks;
    }

    public WebhooksPostServeActionDefinition addWebhook(WebhookPostServeActionDefinition webhook) {
        webhooks.add(webhook);
        return this;
    }
}
