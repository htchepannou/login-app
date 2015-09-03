package com.tchepannou.app.login.service.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.tchepannou.app.login.client.v1.TransactionInfo;
import com.tchepannou.app.login.service.Command;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.app.login.service.HttpFactory;
import com.tchepannou.core.http.Http;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;

public abstract class AbstractCommand<I, O> implements Command<I, O> {
    //-- Attributes
    @Autowired
    private MetricRegistry metrics;

    @Autowired
    private HttpFactory httpFactory;

    private CloseableHttpClient httpClient;

    private TransactionInfo transactionInfo;


    //-- Abstract
    protected abstract O doExecute (I request, CommandContext context) throws IOException;

    protected abstract String getMetricName ();

    //-- Command Override
    @Override
    public O execute(I request, CommandContext context) throws IOException {
        final String metricName = getMetricName();
        final Timer.Context timer = metrics.timer(metricName + "-duration").time();
        try {
            metrics.meter(metricName).mark();

            this.httpClient = httpFactory.newHttpClient();
            try {
                this.transactionInfo = newTransactionInfo(context);

                /* pre */
                authenticate(context);

                /* execute */
                O response = doExecute(request, context);

                /* post */
                return response;
            } finally {
                httpClient.close();
            }
        } catch (RuntimeException e) {
            metrics.meter(metricName + "-errors").mark();

            throw e;
        } finally {
            timer.stop();
        }
    }

    //-- Protected
    protected Logger getLogger () {
        return LoggerFactory.getLogger(getClass());
    }

    protected TransactionInfo getTransactionInfo (){
        return transactionInfo;
    }

    protected void authenticate (CommandContext context) throws IOException {
    }

    protected HttpClient getHttpClient () {
        return httpClient;
    }

    protected Http newHttp (final HttpClient client){
        return httpFactory
                .newHttp(client)
                .header(Http.HEADER_TRANSACTION_ID, getTransactionInfo().getTransactionId())
        ;
    }

    //-- Private
    private TransactionInfo newTransactionInfo (final CommandContext context){
        return new TransactionInfo()
                .withTransactionId(context.getTransactionId())
                .withDateTime(new Date())
        ;
    }

}
