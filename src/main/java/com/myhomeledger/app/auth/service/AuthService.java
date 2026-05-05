package com.myhomeledger.app.auth.service;

import com.myhomeledger.app.auth.dto.*;

public interface AuthService {

    TokenResponse signup(SignupRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse refresh(RefreshRequest request);

    void logout(LogoutRequest request);
}
