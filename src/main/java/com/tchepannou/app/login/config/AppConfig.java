package com.tchepannou.app.login.config;

import com.tchepannou.app.login.service.HttpFactory;
import com.tchepannou.app.login.service.impl.HttpFactoryImpl;
import com.tchepannou.app.login.service.impl.LoginCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declare your services here!
 */
@Configuration
public class AppConfig {
    @Bean
    HttpFactory httpFactory(){
        return new HttpFactoryImpl();
    }

    @Bean
    LoginCommand loginCommand(){
        return new LoginCommand();
    }
}
