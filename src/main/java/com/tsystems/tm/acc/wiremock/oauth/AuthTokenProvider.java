package com.tsystems.tm.acc.wiremock.oauth;

public interface AuthTokenProvider {
    String getToken();

    void revokeToken();
}
