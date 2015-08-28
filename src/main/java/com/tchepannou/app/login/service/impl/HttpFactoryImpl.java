package com.tchepannou.app.login.service.impl;

import com.tchepannou.app.login.service.HttpFactory;
import com.tchepannou.core.http.Http;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class HttpFactoryImpl implements HttpFactory {
    @Autowired
    private Jackson2ObjectMapperBuilder jackson;

    @Override
    public CloseableHttpClient newHttpClient() {
        return HttpClients.createDefault();
    }

    @Override
    public Http newHttp(HttpClient client) {
        return new Http()
                .withObjectMapper(jackson.build())
                .withHttpClient(client)
        ;
    }
}
