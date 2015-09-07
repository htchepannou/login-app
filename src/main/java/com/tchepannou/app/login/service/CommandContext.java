package com.tchepannou.app.login.service;

public interface CommandContext {
    String getAccessTokenId();
    String getTransactionId();
    long getId ();
    int getLimit ();
    int getOffset ();
}
