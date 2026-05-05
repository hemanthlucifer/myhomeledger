package com.myhomeledger.app.security.entity;

import com.myhomeledger.app.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "user_authentication")
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthentication {

    @Id
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "is_account_locked", nullable = false)
    private boolean accountLocked;
}
