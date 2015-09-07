package com.tchepannou.app.login.service.blog;

import com.tchepannou.app.login.client.v1.Constants;
import com.tchepannou.app.login.client.v1.blog.AppPostCollectionResponse;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.app.login.service.impl.AbstractSecuredCommand;
import com.tchepannou.blog.client.v1.PostCollectionResponse;
import com.tchepannou.blog.client.v1.PostResponse;
import com.tchepannou.blog.client.v1.SearchRequest;
import com.tchepannou.core.http.Http;
import com.tchepannou.party.client.v1.PartyCollectionRequest;
import com.tchepannou.party.client.v1.PartyCollectionResponse;
import com.tchepannou.party.client.v1.PartyResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractSearchPostCommand extends AbstractSecuredCommand<Void, AppPostCollectionResponse> {
    //-- Attributes
    @Value("${party.hostname}")
    private String partyHostname;

    @Value("${party.port}")
    private int partyPort;

    @Value("${blog.hostname}")
    private String blogHostname;

    @Value("${blog.port}")
    private int blogPort;

    //-- AbstractSecuredCommand overrides
    @Override
    protected AppPostCollectionResponse doExecute(Void req, CommandContext context) throws IOException {
        final SearchRequest request = createSearchRequest(context);

        final List<PostResponse> posts = getPosts(request);

        final Map<Long, PartyResponse> teams = getTeams(posts);

        return new AppPostCollectionResponse(getTransactionInfo(), posts, teams);
    }

    //-- Protected
    protected SearchRequest createSearchRequest (final CommandContext context) throws IOException {
        final SearchRequest request = new SearchRequest();
        request.setStatus("published");
        request.setBlogIds(getTeamIds(context));
        request.setLimit(context.getLimit());
        request.setOffset(context.getOffset());

        return request;
    }

    protected abstract List<Long> getTeamIds (final CommandContext context) throws IOException;


    //-- Getter
    public String getPartyHostname() {
        return partyHostname;
    }

    public int getPartyPort() {
        return partyPort;
    }

    public String getBlogHostname() {
        return blogHostname;
    }

    public int getBlogPort() {
        return blogPort;
    }

    //-- Private
    private List<PostResponse> getPosts (final SearchRequest request) throws IOException{
        return getHttp()
                .header(Http.HEADER_ACCESS_TOKEN, getAccessToken().getId())
                .withHost(blogHostname)
                .withPort(blogPort)
                .withPath(Constants.URI_BLOG + "search")
                .withPayload(request)
                .post(PostCollectionResponse.class)
                .getPosts()
        ;
    }

    private Map<Long, PartyResponse> getTeams (final List<PostResponse> posts) throws IOException {
        final Set<Long> ids = posts.stream()
                .map(post -> post.getBlogId())
                .collect(Collectors.toSet());

        PartyCollectionRequest request = new PartyCollectionRequest();
        request.setIds(new ArrayList<>(ids));
        return getHttp()
                .withHost(partyHostname)
                .withPort(partyPort)
                .withPath(Constants.URI_PARTY + "list")
                .withPayload(request)
                .post(PartyCollectionResponse.class)
                .getParties()
                .stream()
                .collect(Collectors.toMap(p -> p.getId(), p -> p))
        ;

    }
}
