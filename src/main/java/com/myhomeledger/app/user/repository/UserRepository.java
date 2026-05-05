package com.myhomeledger.app.user.repository;

import com.myhomeledger.app.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("Select count(u) > 0 from UserEntity u where u.phoneNumber = :phoneNumber and u.userId != :userId")
    boolean existsByPhoneNumberAndUserIdNot(String phoneNumber, UUID userId);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

}
