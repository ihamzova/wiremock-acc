package com.tsystems.tm.acc;

import com.github.jknack.handlebars.Options;
import com.github.tomakehurst.wiremock.extension.responsetemplating.helpers.HandlebarsHelper;

import java.io.IOException;

public class Oauth2Helper extends HandlebarsHelper<Object> {
    private AuthTokenProvider tokenProvider;

    public Oauth2Helper() {
        String clientId = System.getenv("CLIENT_ID");
        String clientSecret = System.getenv("CLIENT_SECRET");
        this.tokenProvider = new RhssoClientFlowAuthTokenProvider(clientId, clientSecret);
    }

    @Override
    public String apply(Object o, Options options) throws IOException {
        return tokenProvider.getToken();
    }
}
