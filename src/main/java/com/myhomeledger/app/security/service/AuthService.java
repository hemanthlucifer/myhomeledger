package com.myhomeledger.app.security.service;

import com.myhomeledger.app.security.dto.*;

public interface AuthService {

    TokenResponse signup(SignupRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse refresh(RefreshRequest request);

    void logout(LogoutRequest request);
}
