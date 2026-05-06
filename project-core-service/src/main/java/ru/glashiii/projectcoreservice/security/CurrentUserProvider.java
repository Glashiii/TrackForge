package ru.glashiii.projectcoreservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.glashiii.projectcoreservice.exceptions.UnauthorizedException;


@Component
public class CurrentUserProvider {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserPrincipal currentUser)) {
            throw new UnauthorizedException("Invalid authentication principal");
        }

        return currentUser.getId();
    }
}
