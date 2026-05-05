package com.myhomeledger.app.security.entity;

import com.myhomeledger.app.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_sessions")
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    @Id
    @SequenceGenerator(name = "session_id_seq", sequenceName = "session_id_seq", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_id_seq")
    private long sessionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "refresh_token_hash", nullable = false, unique = true, length = 64)
    private String refreshTokenHash;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    public UUID getUserId() {
        return user != null ? user.getUserId() : null;
    }
}
