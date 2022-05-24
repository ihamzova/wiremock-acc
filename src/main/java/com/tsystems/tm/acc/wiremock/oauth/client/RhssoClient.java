package com.tsystems.tm.acc.wiremock.oauth.client;


import com.tsystems.tm.acc.wiremock.oauth.client.invoker.ApiClient;
import com.tsystems.tm.acc.wiremock.oauth.client.invoker.GsonObjectMapper;
import com.tsystems.tm.acc.wiremock.oauth.client.invoker.JSON;

import java.net.URI;

public class RhssoClient {
    private ApiClient client;
    private String baseUrl;

    public RhssoClient() {
        this.baseUrl = String.format("%s/realms/%s/protocol/openid-connect", System.getenv("RHSSO_HOST"), System.getenv("OAUTH_REALM"));
        this.client = ApiClient.api(ApiClient.Config.apiConfig().reqSpecSupplier(
                () -> RequestSpecBuilders.getDefault(GsonObjectMapper.gson(), URI.create(baseUrl))));
    }

    public ApiClient getClient() {
        return client;
    }

    public static JSON json() {
        return new JSON();
    }
}
