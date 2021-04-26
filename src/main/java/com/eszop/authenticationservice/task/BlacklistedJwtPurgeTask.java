package com.eszop.authenticationservice.task;

import com.eszop.authenticationservice.repository.BlacklistedJwtRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class BlacklistedJwtPurgeTask {

    private final BlacklistedJwtRepository blacklistedJwtRepository;

    public BlacklistedJwtPurgeTask(BlacklistedJwtRepository blacklistedJwtRepository) {
        this.blacklistedJwtRepository = blacklistedJwtRepository;
    }

    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {
        Date now = Date.from(Instant.now());
        blacklistedJwtRepository.deleteAllByExpirationDateBefore(now);
    }
}
