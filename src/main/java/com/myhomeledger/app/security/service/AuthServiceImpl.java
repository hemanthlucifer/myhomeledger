package com.myhomeledger.app.security.service;

import com.myhomeledger.app.security.config.JwtProperties;
import com.myhomeledger.app.security.dto.*;
import com.myhomeledger.app.security.entity.UserAuthentication;
import com.myhomeledger.app.security.entity.UserSession;
import com.myhomeledger.app.security.exception.DuplicatePhoneException;
import com.myhomeledger.app.security.exception.InvalidCredentialsException;
import com.myhomeledger.app.security.exception.InvalidRefreshTokenException;
import com.myhomeledger.app.security.jwt.JwtService;
import com.myhomeledger.app.security.repository.UserAuthenticationRepository;
import com.myhomeledger.app.security.repository.UserSessionRepository;
import com.myhomeledger.app.security.support.TokenHasher;
import com.myhomeledger.app.user.entity.UserEntity;
import com.myhomeledger.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public TokenResponse signup(SignupRequest request) {
        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new DuplicatePhoneException();
        }

        Instant now = Instant.now();
        UserEntity user = new UserEntity();
        user.setUserName(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userRepository.save(user);

        UserAuthentication authentication = new UserAuthentication();
        authentication.setUser(user);
        authentication.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        authentication.setAccountLocked(false);
        userAuthenticationRepository.save(authentication);

        return issueTokensForUser(user);
    }

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(InvalidCredentialsException::new);
        UserAuthentication auth = userAuthenticationRepository.findById(user.getUserId())
                .orElseThrow(InvalidCredentialsException::new);
        if (auth.isAccountLocked()) {
            throw new InvalidCredentialsException();
        }
        if (!passwordEncoder.matches(request.getPassword(), auth.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return issueTokensForUser(user);
    }

    @Override
    @Transactional
    public TokenResponse refresh(RefreshRequest request) {
        String hash = TokenHasher.sha256Hex(request.getRefreshToken());
        UserSession session = userSessionRepository.findByRefreshTokenHash(hash)
                .orElseThrow(InvalidRefreshTokenException::new);
        if (session.getExpiresAt().isBefore(Instant.now())) {
            userSessionRepository.delete(session);
            throw new InvalidRefreshTokenException();
        }

        String newRawRefresh = newSecureRefreshToken();
        String newHash = TokenHasher.sha256Hex(newRawRefresh);
        session.setRefreshTokenHash(newHash);
        session.setExpiresAt(Instant.now().plusSeconds(jwtProperties.refreshTokenDays() * 86400));
        userSessionRepository.save(session);

        UserEntity user = session.getUser();
        String access = jwtService.createAccessToken(user.getUserId(), session.getSessionId());
        return new TokenResponse(
                access,
                newRawRefresh,
                jwtProperties.accessTokenMinutes() * 60
        );
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        String hash = TokenHasher.sha256Hex(request.getRefreshToken());
        userSessionRepository.findByRefreshTokenHash(hash)
                .ifPresent(userSessionRepository::delete);
    }

    private TokenResponse issueTokensForUser(UserEntity user) {
        String rawRefresh = newSecureRefreshToken();
        String refreshHash = TokenHasher.sha256Hex(rawRefresh);
        Instant now = Instant.now();

        UserSession session = new UserSession();
        session.setUser(user);
        session.setRefreshTokenHash(refreshHash);
        session.setCreatedAt(now);
        session.setExpiresAt(now.plusSeconds(jwtProperties.refreshTokenDays() * 86400));
        userSessionRepository.save(session);

        String access = jwtService.createAccessToken(user.getUserId(), session.getSessionId());
        return new TokenResponse(
                access,
                rawRefresh,
                jwtProperties.accessTokenMinutes() * 60
        );
    }

    private static String newSecureRefreshToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
