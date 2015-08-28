package com.tchepannou.app.login.service;

import com.tchepannou.core.http.Http;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpFactory {
    CloseableHttpClient newHttpClient ();

    Http newHttp (HttpClient client);
}
