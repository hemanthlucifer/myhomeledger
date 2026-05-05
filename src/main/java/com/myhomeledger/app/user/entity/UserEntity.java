package com.myhomeledger.app.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @UuidGenerator
    @Column(name = "user_id", columnDefinition = "uuid")
    private UUID userId;

    @Column(name = "user_name", nullable = false)
    @Size(min = 3, max = 50)
    private String userName;

    @Column(name = "phone_number", nullable = false, unique = true)
    @Size(min = 10, max = 10)
    private String phoneNumber;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
