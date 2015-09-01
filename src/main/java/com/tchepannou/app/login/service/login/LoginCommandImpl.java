package com.tchepannou.app.login.service.login;

import com.tchepannou.app.login.client.v1.AppErrors;
import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.client.v1.login.AppLoginRequest;
import com.tchepannou.app.login.client.v1.login.AppLoginResponse;
import com.tchepannou.app.login.exception.LoginException;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.app.login.service.HttpFactory;
import com.tchepannou.app.login.service.LoginCommand;
import com.tchepannou.app.login.service.impl.AbstractCommand;
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
public class LoginCommandImpl extends AbstractCommand<AppLoginRequest, AppLoginResponse> implements LoginCommand {
    //-- Attributes
    private static final Logger LOG = LoggerFactory.getLogger(LoginCommandImpl.class);

    @Value("${auth.hostname}")
    private String authHostname;

    @Value("${auth.port}")
    private int authPort;

    @Autowired
    private HttpFactory httpFactory;


    //-- Command overrides
    @Override
    protected
    AppLoginResponse doExecute(AppLoginRequest request, CommandContext context) throws IOException {
        try (CloseableHttpClient client = httpFactory.newHttpClient()) {

            Http http = newHttp(client);

            AccessTokenResponse accessToken = login(request, http);

            return new AppLoginResponse(getTransactionInfo(), accessToken);

        } catch (HttpException e){
            final int status = e.getStatus();

            if (status == 409){
                throw new LoginException(AppErrors.AUTH_FAILED, e);
            } else {
                throw e;
            }
        }
    }

    @Override
    protected String getMetricName() {
        return Constants.METRIC_LOGIN;
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

    private Http newHttp (final HttpClient client){
        return httpFactory
                .newHttp(client)
                .header(Http.HEADER_TRANSACTION_ID, getTransactionInfo().getTransactionId())
                ;
    }
}
