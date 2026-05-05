package com.myhomeledger.app.security.repository;

import com.myhomeledger.app.security.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, UUID> {
}
