package com.tchepannou.app.login.controller;

import com.tchepannou.app.login.service.CommandContext;

import java.util.UUID;

public class CommandContextImpl implements CommandContext {
    //-- Attributes
    private String accessTokenId;
    private String transactionId = UUID.randomUUID().toString();
    private int limit;
    private int offset;

    //-- Public
    public CommandContextImpl withAccessTokenId(String id){
        this.accessTokenId = id;
        return this;
    }

    public CommandContextImpl withTransactionId (String transactionId){
        this.transactionId = transactionId;
        return this;
    }

    public CommandContextImpl withLimit (int limit){
        this.limit = limit;
        return this;
    }

    public CommandContextImpl withOffset (int offset){
        this.offset = offset;
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

    @Override public int getLimit() {
        return limit;
    }

    @Override public int getOffset() {
        return offset;
    }
}
