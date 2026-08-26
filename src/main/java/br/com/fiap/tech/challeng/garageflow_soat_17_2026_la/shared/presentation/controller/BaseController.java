package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.presentation.controller;

import br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.config.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseController {

    protected String resolveCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthenticatedUser authenticatedUser) {
            if (authenticatedUser.userId() == null || authenticatedUser.userId().isBlank()) {
                throw new IllegalArgumentException("User ID not present in token");
            }
            return authenticatedUser.userId();
        }

        throw new IllegalArgumentException("User not authenticated");
    }
}
