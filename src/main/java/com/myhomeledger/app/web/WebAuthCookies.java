package com.myhomeledger.app.web;

import com.myhomeledger.app.security.config.AuthCookieNames;
import com.myhomeledger.app.security.config.JwtProperties;
import com.myhomeledger.app.security.dto.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

final class WebAuthCookies {

    private WebAuthCookies() {}

    static void writeAuthCookies(HttpServletResponse response, TokenResponse tokens, JwtProperties jwtProperties, boolean secure) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                ResponseCookie.from(AuthCookieNames.ACCESS_TOKEN, tokens.accessToken())
                        .httpOnly(true)
                        .secure(secure)
                        .path("/")
                        .maxAge(tokens.accessExpiresInSeconds())
                        .sameSite("Lax")
                        .build()
                        .toString());
        long refreshMaxAge = jwtProperties.refreshTokenDays() * 86400L;
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                ResponseCookie.from(AuthCookieNames.REFRESH_TOKEN, tokens.refreshToken())
                        .httpOnly(true)
                        .secure(secure)
                        .path("/")
                        .maxAge(refreshMaxAge)
                        .sameSite("Lax")
                        .build()
                        .toString());
    }

    static void clearAuthCookies(HttpServletResponse response, boolean secure) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                ResponseCookie.from(AuthCookieNames.ACCESS_TOKEN, "")
                        .httpOnly(true)
                        .secure(secure)
                        .path("/")
                        .maxAge(0)
                        .sameSite("Lax")
                        .build()
                        .toString());
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                ResponseCookie.from(AuthCookieNames.REFRESH_TOKEN, "")
                        .httpOnly(true)
                        .secure(secure)
                        .path("/")
                        .maxAge(0)
                        .sameSite("Lax")
                        .build()
                        .toString());
    }
}
