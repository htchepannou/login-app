package com.tchepannou.app.login.service.team;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.client.v1.team.AppTeamResponse;
import com.tchepannou.app.login.exception.NotFoundException;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.app.login.service.impl.AbstractSecuredCommand;
import com.tchepannou.core.http.HttpException;
import com.tchepannou.party.client.v1.PartyResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class GetTeamProfile extends AbstractSecuredCommand<Void, AppTeamResponse> {
    //-- Attributes
    @Value("${party.hostname}")
    private String partyHostname;

    @Value("${party.port}")
    private int partyPort;

    //-- AbstractSecuredCommand overrides
    @Override
    protected AppTeamResponse doExecute(Void request, CommandContext context) throws IOException {
        try {
            PartyResponse response = getHttp()
                    .withPort(partyPort)
                    .withHost(partyHostname)
                    .withPath(Constants.URI_PARTY + context.getId())
                    .get(PartyResponse.class);

            return new AppTeamResponse(getTransactionInfo(), response);
        } catch (HttpException e){
            final int status = e.getStatus();

            if (status == 404){
                throw new NotFoundException(Constants.ERROR_NOT_FOUND, e);
            } else {
                throw e;
            }
        }
    }

    @Override
    protected String getMetricName() {
        return Constants.METRIC_GET_TEAM;
    }
}
