package com.tchepannou.app.login.service.blog;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.party.client.v1.PartyCollectionResponse;
import com.tchepannou.party.client.v1.PartyResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class GetMyPostsCommand extends AbstractSearchPostCommand {

    //-- AbstractSearchPostCommand overrides
    @Override
    protected String getMetricName() {
        return Constants.METRIC_MY_DASHBOARD;
    }

    @Override
    protected List<Long> getTeamIds (CommandContext context) throws IOException {
        return getHttp()
                .withPort(getPartyPort())
                .withHost(getPartyHostname())
                .withPath(Constants.URI_PARTY + "/from/" + getUserId () + "/relation/member")
                .get(PartyCollectionResponse.class)
                .getParties()
                .stream()
                .map(PartyResponse::getId)
                .collect(Collectors.toList())
                ;
    }
}
