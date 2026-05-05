package com.myhomeledger.app.user.repository;

import com.myhomeledger.app.user.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, UUID> {
}
