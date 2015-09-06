package com.tchepannou.app.login.service.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.tchepannou.app.login.client.v1.TransactionInfo;
import com.tchepannou.app.login.service.Command;
import com.tchepannou.app.login.service.CommandContext;
import com.tchepannou.core.http.Http;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.Date;

public abstract class AbstractCommand<I, O> implements Command<I, O> {
    //-- Attributes
    @Autowired
    private MetricRegistry metrics;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    private Http http;
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

            this.transactionInfo = newTransactionInfo(context);
            getHttp().header(Http.HEADER_TRANSACTION_ID, transactionInfo.getTransactionId());

            /* pre */
            authenticate(context);

            /* execute */
            O response = doExecute(request, context);

            /* post */
            return response;

        } catch (RuntimeException e) {
            metrics.meter(metricName + "-errors").mark();

            throw e;
        } finally {
            timer.stop();
        }
    }

    //-- Protected
    protected TransactionInfo getTransactionInfo (){
        return transactionInfo;
    }

    protected void authenticate (CommandContext context) throws IOException {
    }

    protected Http getHttp (){
        if (http == null){
            return new Http()
                    .withHttpClient(httpClient)
                    .header(Http.HEADER_TRANSACTION_ID, transactionInfo.getTransactionId())
                    .withObjectMapper(jackson2ObjectMapperBuilder.build())
            ;
        }
        return http;
    }

    //-- Private
    private TransactionInfo newTransactionInfo (final CommandContext context){
        return new TransactionInfo()
                .withTransactionId(context.getTransactionId())
                .withDateTime(new Date())
        ;
    }

}
