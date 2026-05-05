package com.myhomeledger.app.security.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long accessExpiresInSeconds
) {
}
