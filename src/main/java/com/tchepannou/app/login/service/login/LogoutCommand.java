package com.tchepannou.app.login.service.login;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.app.login.service.impl.AbstractSecuredCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class LogoutCommand extends AbstractSecuredCommand<Void, Void> {
    //-- Attributes
    @Value("${auth.hostname}")
    private String authHostname;

    @Value("${auth.port}")
    private int authPort;


    //-- Command overrides
    @Override
    protected Void doExecute(Void request, CommandContext context) throws IOException {
        getHttp()
                .withPort(authPort)
                .withHost(authHostname)
                .withPath(Constants.URI_AUTH + "logout/")
                .withPayload(request)
                .post()
        ;

        return null;
    }

    @Override
    protected String getMetricName() {
        return Constants.METRIC_LOGOUT;
    }
}
