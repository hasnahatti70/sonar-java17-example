package com.sipios.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * An entry point that is used to return a 401 on unauthenticated access to resources.
 *
 * The entry point will be used to override the default one that send a 302 Redirect
 */
@Component
public final class RestAuthenticationEntryPoint
    implements AuthenticationEntryPoint {

    /**
     * Overrides the default behaviour to return a 401 Response
     *
     * @param request Http Request
     * @param response Http Response
     * @param authException Auth Exception
     * @throws IOException If an input or output exception occurs
     */
    @Override
    public void commence(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException authException) throws IOException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
