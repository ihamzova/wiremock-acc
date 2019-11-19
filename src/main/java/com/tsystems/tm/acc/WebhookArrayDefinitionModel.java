package com.tsystems.tm.acc;

import java.util.List;

public class WebhookArrayDefinitionModel {
    private List<WebhookDefinition> webhooks;

    public WebhookArrayDefinitionModel(List<WebhookDefinition> webhooks) {
        this.webhooks = webhooks;
    }
}
