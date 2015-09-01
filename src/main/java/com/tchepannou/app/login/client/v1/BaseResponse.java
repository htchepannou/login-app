package com.tchepannou.app.login.client.v1;

public class BaseResponse {
    private TransactionInfo transactionInfo;


    public BaseResponse() {
    }
    public BaseResponse(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }
}
