package com.lincz.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Configuration
public class AuditorAwareConfiguration implements AuditorAware<LocalDateTime> {

    @Override
    public Optional<LocalDateTime> getCurrentAuditor() {
        return Optional.of(LocalDateTime.now());
    }
}
