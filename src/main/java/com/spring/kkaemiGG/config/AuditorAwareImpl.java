package com.spring.kkaemiGG.config;

import com.spring.kkaemiGG.domain.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Nonnull;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Nonnull
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null == authentication || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        User user = (User) authentication.getPrincipal();
        return Optional.of(user.getId().toString());
    }
}
