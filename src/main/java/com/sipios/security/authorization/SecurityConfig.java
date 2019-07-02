package com.sipios.security.authorization;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * A class that will be used in a @PreAuthorize
 * @see org.springframework.security.access.prepost.PreAuthorize
 */
@Component
public class SecurityConfig {

    /**
     * Example of a function that can be run inside a SpEL function
     * @see <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions">https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions</a>
     *
     * @param authentication The current authentication bean
     * @return whether the curent user is authenticated
     */
    public boolean isRegistered(Authentication authentication) {
        return authentication.isAuthenticated();
    }
}
