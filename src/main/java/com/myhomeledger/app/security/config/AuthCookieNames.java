package com.myhomeledger.app.security.config;

/**
 * HttpOnly cookies used by the Thymeleaf UI for the same JWTs as the REST API.
 */
public final class AuthCookieNames {

    public static final String ACCESS_TOKEN = "mh_access_token";
    public static final String REFRESH_TOKEN = "mh_refresh_token";

    private AuthCookieNames() {}
}
