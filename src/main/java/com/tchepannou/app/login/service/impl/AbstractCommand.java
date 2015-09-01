package com.tchepannou.app.login.service.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.tchepannou.app.login.client.v1.TransactionInfo;
import com.tchepannou.app.login.service.Command;
import com.tchepannou.app.login.service.CommandContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;

public abstract class AbstractCommand<I, O> implements Command<I, O> {
    //-- Attributes
    private Logger logger;  // NOSONAR

    @Autowired
    private MetricRegistry metrics;

    private TransactionInfo transactionInfo;

    //-- Constructor
    public AbstractCommand(){
        this.logger = LoggerFactory.getLogger(getClass());
    }

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

            /* execute */
            this.transactionInfo = newTransactionInfo(context);
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
    protected Logger getLogger () {
        return logger;
    }

    protected TransactionInfo getTransactionInfo (){
        return transactionInfo;
    }

    private TransactionInfo newTransactionInfo (final CommandContext context){
        return new TransactionInfo()
                .withTransactionId(context.getTransactionId())
                .withDateTime(new Date())
        ;
    }

}
