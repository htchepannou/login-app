package com.tchepannou.app.login.config;

import com.tchepannou.app.login.service.login.LoginCommand;
import com.tchepannou.app.login.service.login.LogoutCommand;
import com.tchepannou.app.login.service.profile.GetProfileCommand;
import com.tchepannou.app.login.service.team.MyTeamsCommand;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declare your services here!
 */
@Configuration
public class AppConfig {
    @Bean
    HttpClientConnectionManager httpClientConnectionManager (){
        PoolingHttpClientConnectionManager cnn = new PoolingHttpClientConnectionManager();
        cnn.setMaxTotal(50);
        cnn.setDefaultMaxPerRoute(10);
        return cnn;
    }

    @Bean
    HttpClient httpClient () {
        return HttpClients.custom()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                        .setStaleConnectionCheckEnabled(true)
                        .setSocketTimeout(5000)
                        .setConnectionRequestTimeout(5000)
                        .build()
            ).setConnectionManager(httpClientConnectionManager())
            .build();
    }

    @Bean
    LoginCommand loginCommand(){
        return new LoginCommand();
    }

    @Bean
    LogoutCommand logoutCommand(){
        return new LogoutCommand();
    }

    @Bean
    GetProfileCommand getProfileCommand(){
        return new GetProfileCommand();
    }

    @Bean
    MyTeamsCommand myTeamsCommand(){
        return new MyTeamsCommand();
    }
}
