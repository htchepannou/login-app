package com.tchepannou.app.login.service;

public interface CommandContext {
    String getAccessTokenId();
    String getTransactionId();
    int getLimit ();
    int getOffset ();
}
