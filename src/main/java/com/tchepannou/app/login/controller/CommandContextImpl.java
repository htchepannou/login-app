package com.tchepannou.app.login.controller;

import com.tchepannou.app.login.service.CommandContext;

import java.util.UUID;

public class CommandContextImpl implements CommandContext {
    //-- Attributes
    private String accessTokenId;
    private String transactionId = UUID.randomUUID().toString();

    //-- Public
    public CommandContextImpl withAccessTokenId(String id){
        this.accessTokenId = id;
        return this;
    }

    public CommandContextImpl withTransactionId (String transactionId){
        this.transactionId = transactionId;
        return this;
    }

    //-- CommandContext overrides
    @Override public String getAccessTokenId() {
        return accessTokenId;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }
}
