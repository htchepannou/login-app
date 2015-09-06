package com.tchepannou.app.login.client.v1.team;

import com.tchepannou.app.login.client.v1.BaseResponse;
import com.tchepannou.app.login.client.v1.TransactionInfo;
import com.tchepannou.party.client.v1.PartyCollectionResponse;
import com.tchepannou.party.client.v1.PartyResponse;

import java.util.List;

public class AppTeamCollectionResponse extends BaseResponse{
    private List<PartyResponse> teams;

    public AppTeamCollectionResponse(TransactionInfo transactionInfo, PartyCollectionResponse teams) {
        super(transactionInfo);
        this.teams = teams.getParties();
    }

    public List<PartyResponse> getTeams() {
        return teams;
    }

    public void setTeams(List<PartyResponse> teams) {
        this.teams = teams;
    }
}
