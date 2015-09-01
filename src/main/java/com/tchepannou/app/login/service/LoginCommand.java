package com.tchepannou.app.login.service;

import com.tchepannou.app.login.client.v1.login.AppLoginRequest;
import com.tchepannou.app.login.client.v1.login.AppLoginResponse;

public interface LoginCommand extends Command<AppLoginRequest, AppLoginResponse>{
}
