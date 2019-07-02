package com.sipios.security;

import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A redirect strategy that does nothing
 *
 * This strategy is used to override the default redirect strategy that is usually done after:
 * - A successful authentication
 * - An error during the authentication
 */
public class NoRedirectStrategy implements RedirectStrategy {

    /**
     * Does nothing to return a 200 OK response
     *
     * @param request Http Request
     * @param response Http Response
     * @param url The url to redirect to
     * @throws IOException If an input or output exception occurs
     */
    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {

    }
}
