package com.gracefullyugly.common.security;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.info("getCurrentAuditor: anonymousUser");
            return Optional.of("임시 사용자");
        }

        log.info("getCurrentAuditor: {}", authentication.getName());
        return Optional.ofNullable(authentication.getName());
    }

}
