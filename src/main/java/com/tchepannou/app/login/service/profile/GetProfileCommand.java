package com.tchepannou.app.login.service.profile;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.client.v1.profile.AppProfileResponse;
import com.tchepannou.app.login.exception.LoginException;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.app.login.service.impl.AbstractSecuredCommand;
import com.tchepannou.core.http.Http;
import com.tchepannou.core.http.HttpException;
import com.tchepannou.party.client.v1.PartyResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class GetProfileCommand extends AbstractSecuredCommand<Void, AppProfileResponse> {
    //-- Attributes
    @Value("${party.hostname}")
    private String partyHostname;

    @Value("${party.port}")
    private int partyPort;

    //-- GetProfileCommand overrides
    @Override
    protected AppProfileResponse doExecute(Void request, CommandContext context) throws IOException {
        try {
            Http http = newHttp(getHttpClient());

            PartyResponse party = http
                    .withPort(partyPort)
                    .withHost(partyHostname)
                    .withPath("/v1/party/" + getUserId ())
                    .withPayload(request)
                    .get(PartyResponse.class)
            ;
            return new AppProfileResponse(getTransactionInfo(), party);

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
        return Constants.METRIC_GET_PROFILE;
    }
}
