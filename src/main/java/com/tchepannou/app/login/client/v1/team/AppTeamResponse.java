package com.tchepannou.app.login.client.v1.team;

import com.tchepannou.app.login.client.v1.BaseResponse;
import com.tchepannou.app.login.client.v1.TransactionInfo;
import com.tchepannou.party.client.v1.PartyResponse;

public class AppTeamResponse extends BaseResponse {
    private PartyResponse team;

    public AppTeamResponse(TransactionInfo transactionInfo, PartyResponse team) {
        super(transactionInfo);
        this.team = team;
    }

    public PartyResponse getTeam() {
        return team;
    }
}
