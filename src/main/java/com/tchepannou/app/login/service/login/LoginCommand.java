package com.tchepannou.app.login.service.login;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.client.v1.login.AppLoginRequest;
import com.tchepannou.app.login.client.v1.login.AppLoginResponse;
import com.tchepannou.app.login.exception.LoginException;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.app.login.service.impl.AbstractCommand;
import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.core.http.Http;
import com.tchepannou.core.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class LoginCommand extends AbstractCommand<AppLoginRequest, AppLoginResponse> {
    //-- Attributes
    private static final Logger LOG = LoggerFactory.getLogger(LoginCommand.class);

    @Value("${auth.hostname}")
    private String authHostname;

    @Value("${auth.port}")
    private int authPort;


    //-- Command overrides
    @Override
    protected AppLoginResponse doExecute(AppLoginRequest request, CommandContext context) throws IOException {
        try {
            Http http = newHttp(getHttpClient());

            AccessTokenResponse accessToken =  http
                    .withPort(authPort)
                    .withHost(authHostname)
                    .withPath("/v1/auth/login")
                    .withPayload(request)
                    .post(AccessTokenResponse.class)
            ;

            return new AppLoginResponse(getTransactionInfo(), accessToken);

        } catch (HttpException e){
            final int status = e.getStatus();

            if (status == 409){
                throw new LoginException(Constants.ERROR_AUTH_FAILED, e);
            } else {
                throw e;
            }
        }
    }

    @Override
    protected String getMetricName() {
        return Constants.METRIC_LOGIN;
    }
}
