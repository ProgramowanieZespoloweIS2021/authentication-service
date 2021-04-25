package com.eszop.authenticationservice.repository;

import com.eszop.authenticationservice.domain.BlacklistedJwt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedJwtRepository extends JpaRepository<BlacklistedJwt, String> {
}
