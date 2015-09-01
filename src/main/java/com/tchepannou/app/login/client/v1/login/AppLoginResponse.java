package com.tchepannou.app.login.client.v1.login;

import com.tchepannou.app.login.client.v1.BaseResponse;
import com.tchepannou.app.login.client.v1.TransactionInfo;
import com.tchepannou.auth.client.v1.AccessTokenResponse;

public class AppLoginResponse extends BaseResponse{
    //-- Attributes
    private AccessTokenResponse accessToken;

    //-- Constructor
    public AppLoginResponse() {
    }
    public AppLoginResponse(TransactionInfo transactionInfo, AccessTokenResponse accessToken) {
        super(transactionInfo);
        this.accessToken = accessToken;
    }

    //-- Getter/Setter
    public AccessTokenResponse getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessTokenResponse accessToken) {
        this.accessToken = accessToken;
    }
}
