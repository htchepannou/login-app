package com.tchepannou.app.login.client.v1;

public class Constants {
    public static final String URI_ACCESS_TOKEN = "/v1/access_token/";
    public static final String URI_AUTH = "/v1/auth/";
    public static final String URI_PARTY = "/v1/party/";

    public static final String METRIC_LOGIN = "app-login";
    public static final String METRIC_LOGOUT = "app-logout";
    public static final String METRIC_PROFILE_GET = "app-profile-get";
    public static final String METRIC_TEAM_MY = "app-team-my";

    public static final String ERROR_AUTH_FAILED = "authentication_failed";
    public static final String ERROR_NOT_FOUND = "not_found";
    public static final String ERROR_IO = "connection_failed";

    private Constants (){
    }
}
