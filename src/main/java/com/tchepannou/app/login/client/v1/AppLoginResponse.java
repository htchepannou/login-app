package com.tchepannou.app.login.client.v1;

import com.tchepannou.auth.client.v1.AccessTokenResponse;

public class AppLoginResponse {
    //-- Attributes
    private TransactionInfo transactionInfo;
    private AccessTokenResponse accessToken;

    //-- Getter/Setter
    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public AccessTokenResponse getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessTokenResponse accessToken) {
        this.accessToken = accessToken;
    }
}
