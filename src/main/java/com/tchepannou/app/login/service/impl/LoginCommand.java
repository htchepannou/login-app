package com.tchepannou.app.login.service.impl;

import com.tchepannou.app.login.client.v1.AppLoginRequest;
import com.tchepannou.app.login.client.v1.AppLoginResponse;
import com.tchepannou.app.login.client.v1.AppErrors;
import com.tchepannou.app.login.client.v1.TransactionInfo;
import com.tchepannou.app.login.exception.LoginException;
import com.tchepannou.app.login.service.Command;
import com.tchepannou.app.login.service.HttpFactory;
import com.tchepannou.auth.client.v1.AccessTokenResponse;
import com.tchepannou.core.http.Http;
import com.tchepannou.core.http.HttpException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class LoginCommand implements Command<AppLoginRequest, AppLoginResponse> {
    //-- Attributes
    private static final Logger LOG = LoggerFactory.getLogger(LoginCommand.class);

    @Autowired
    private HttpFactory httpFactory;

    @Value("${auth.hostname}")
    private String authHostname;

    @Value("${auth.port}")
    private int authPort;

    //-- Command overrides
    @Override
    public AppLoginResponse execute(AppLoginRequest request) throws IOException {
        TransactionInfo tx = newTransactionInfo();

        try (CloseableHttpClient client = httpFactory.newHttpClient()) {

            Http http = newHttp(tx, client);

            AccessTokenResponse accessToken = login(request, http);

            AppLoginResponse response = new AppLoginResponse();
            response.setAccessToken(accessToken);
            response.setTransactionInfo(tx);
            return response;

        } catch (HttpException e){
            final int status = e.getStatus();

            if (status == 409){
                throw new LoginException(AppErrors.AUTH_FAILED, e);
            } else {
                throw e;
            }
        }
    }


    //-- Private
    private AccessTokenResponse login (final AppLoginRequest request, final Http http) throws IOException {
        LOG.info("Authenticating the user");

        return http
                .withPort(authPort)
                .withHost(authHostname)
                .withPath("/v1/auth/login")
                .withPayload(request)
                .post(AccessTokenResponse.class)
        ;
    }

    private Http newHttp (final TransactionInfo tx, final HttpClient client){
        return httpFactory
                .newHttp(client)
                .header(Http.HEADER_TRANSACTION_ID, tx.getTransactionId())
        ;
    }

    private TransactionInfo newTransactionInfo (){
        return new TransactionInfo();
    }
}
