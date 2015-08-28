package com.tchepannou.app.login.client.v1;

import java.util.Date;
import java.util.UUID;

public class TransactionInfo {
    private String transactionId = UUID.randomUUID().toString();
    private Date dateTime = new Date();

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
