package com.tchepannou.app.login.service.team;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.client.v1.team.AppTeamCollectionResponse;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.app.login.service.impl.AbstractSecuredCommand;
import com.tchepannou.party.client.v1.PartyCollectionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MyTeamsCommand extends AbstractSecuredCommand<Void, AppTeamCollectionResponse> {
    //-- Attributes
    @Value("${party.hostname}")
    private String partyHostname;

    @Value("${party.port}")
    private int partyPort;

    //-- AbstractSecuredCommand overrides
    @Override
    protected AppTeamCollectionResponse doExecute(Void request, CommandContext context) throws IOException {
        PartyCollectionResponse party = getHttp()
                .withPort(partyPort)
                .withHost(partyHostname)
                .withPath(Constants.URI_PARTY + "/from/" + getUserId () + "/relation/member")
                .withPayload(request)
                .get(PartyCollectionResponse.class)
        ;
        return new AppTeamCollectionResponse(getTransactionInfo(), party);
    }

    @Override
    protected String getMetricName() {
        return Constants.METRIC_TEAM_MY;
    }
}
