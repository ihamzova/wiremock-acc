package com.tsystems.tm.acc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class WebhookArrayDefinition {
    private List<WebhookDefinition> webhooks;

    @JsonCreator
    public WebhookArrayDefinition(@JsonProperty("webhooks") ArrayList<WebhookDefinition> webhooks) {
        this.webhooks = webhooks;
    }

    public WebhookArrayDefinition() {
        this.webhooks = new ArrayList<>();
    }

    public List<WebhookDefinition> getWebhooks() {
        return webhooks;
    }

    public WebhookArrayDefinition addWebhook(WebhookDefinition webhook) {
        webhooks.add(webhook);
        return this;
    }

    public WebhookArrayDefinitionModel toModel() {
        return new WebhookArrayDefinitionModel(webhooks);
    }
}
