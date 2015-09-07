package com.tchepannou.app.login.client.v1;

public class Constants {
    public static final String URI_ACCESS_TOKEN = "/v1/access_token/";
    public static final String URI_AUTH = "/v1/auth/";
    public static final String URI_PARTY = "/v1/party/";
    public static final String URI_BLOG = "/v1/blog/";

    public static final String METRIC_LOGIN = "app-login";
    public static final String METRIC_LOGOUT = "app-logout";
    public static final String METRIC_MY_PROFILE = "app-my-profile";
    public static final String METRIC_MY_DASHBOARD = "app-my-dashboard";
    public static final String METRIC_MY_TEAMS = "app-my-teams";
    public static final String METRIC_GET_TEAM = "app-get-team";

    public static final String ERROR_AUTH_FAILED = "authentication_failed";
    public static final String ERROR_NOT_FOUND = "not_found";
    public static final String ERROR_IO = "connection_failed";

    private Constants (){
    }
}
