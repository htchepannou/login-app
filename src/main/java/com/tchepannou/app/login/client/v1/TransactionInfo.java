package com.tchepannou.app.login.client.v1;

import java.util.Date;

public class TransactionInfo {
    private String transactionId;
    private Date dateTime;

    public String getTransactionId() {
        return transactionId;
    }

    public TransactionInfo withTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public TransactionInfo withDateTime(Date dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
