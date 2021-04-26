package com.eszop.authenticationservice.repository;

import com.eszop.authenticationservice.domain.BlacklistedJwt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface BlacklistedJwtRepository extends JpaRepository<BlacklistedJwt, String> {
    void deleteAllByExpirationDateBefore(Date now);
}
