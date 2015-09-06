package com.tchepannou.app.login.service.impl;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.exception.AuthenticationException;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.auth.client.v1.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public abstract class AbstractSecuredCommand<I, O> extends AbstractCommand<I, O> {
    //-- Attributes
    @Value("${auth.hostname}")
    private String authHostname;

    @Value("${auth.port}")
    private int authPort;

    private AccessTokenResponse accessToken;

    //-- AbstractCommand overrides
    @Override
    protected void authenticate(CommandContext context) {
        try {
            this.accessToken = getHttp()
                    .withHost(authHostname)
                    .withPort(authPort)
                    .withPath(Constants.URI_ACCESS_TOKEN + context.getAccessTokenId())
                    .get(AccessTokenResponse.class)
            ;
        } catch (IOException e) {
            throw new AuthenticationException("Unable to resolve accessToken", e);
        }
    }

    //-- Protected
    public long getUserId ()  {
        return getAccessToken ().getUserId ();
    }

    public AccessTokenResponse getAccessToken() {
        return accessToken;
    }
}
