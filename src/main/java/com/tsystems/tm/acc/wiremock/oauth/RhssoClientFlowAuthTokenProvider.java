package com.tsystems.tm.acc.wiremock.oauth;

import com.tsystems.tm.acc.wiremock.oauth.client.RhssoClient;
import com.tsystems.tm.acc.wiremock.oauth.client.model.GrantType;
import com.tsystems.tm.acc.wiremock.oauth.client.model.Token;

import java.time.LocalDateTime;

import static com.tsystems.tm.acc.wiremock.oauth.client.invoker.ResponseSpecBuilders.shouldBeCode;
import static com.tsystems.tm.acc.wiremock.oauth.client.invoker.ResponseSpecBuilders.validatedWith;

public class RhssoClientFlowAuthTokenProvider implements AuthTokenProvider {
    private Token token;
    private LocalDateTime lastFetched;
    private String clientId;
    private String clientSecret;

    public RhssoClientFlowAuthTokenProvider(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String getToken() {
        if (token == null || LocalDateTime.now().isAfter(lastFetched.plusSeconds(token.getExpiresIn() - 15))) {
            fetchToken();
        }
        return token.getAccessToken();
    }

    private void fetchToken() {
        RhssoClient api = new RhssoClient();
        token = api.getClient().rhsso()
                .token()
                .clientIdForm(getClientId())
                .clientSecretForm(getClientSecret())
                .scopeForm("openid")
                .grantTypeForm(GrantType.CLIENT_CREDENTIALS)
                .executeAs(validatedWith(shouldBeCode(200)));
        lastFetched = LocalDateTime.now();
    }

    @Override
    public void revokeToken() {
        token = null;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
