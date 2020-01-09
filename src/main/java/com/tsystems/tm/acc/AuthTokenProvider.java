package com.tsystems.tm.acc;

public interface AuthTokenProvider {
    String getToken();

    void revokeToken();
}
