package com.tchepannou.app.login.client.v1.blog;

import com.tchepannou.app.login.client.v1.BaseResponse;
import com.tchepannou.app.login.client.v1.TransactionInfo;
import com.tchepannou.blog.client.v1.PostResponse;
import com.tchepannou.party.client.v1.PartyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppPostCollectionResponse extends BaseResponse{
    private List<PostResponse> posts = new ArrayList<>();
    private Map<Long, PartyResponse> teams = new HashMap<>();

    public AppPostCollectionResponse(
            final TransactionInfo transactionInfo,
            final List<PostResponse> posts,
            final Map<Long, PartyResponse> teams) {
        super(transactionInfo);
        this.posts = posts;
        this.teams = teams;
    }

    public int getSize() {
        return posts.size();
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public Map<Long, PartyResponse> getTeams() {
        return teams;
    }
}
