package com.tchepannou.app.login.client.v1.profile;

import com.tchepannou.app.login.client.v1.BaseResponse;
import com.tchepannou.app.login.client.v1.TransactionInfo;
import com.tchepannou.party.client.v1.PartyResponse;

public class AppProfileResponse extends BaseResponse{
    private PartyResponse userInfo;

    public AppProfileResponse(TransactionInfo transactionInfo, PartyResponse userInfo) {
        super(transactionInfo);
        this.userInfo = userInfo;
    }

    public PartyResponse getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(PartyResponse userInfo) {
        this.userInfo = userInfo;
    }
}
