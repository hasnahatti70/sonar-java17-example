package com.sipios.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * This class overrides the default Spring security configuration
 */
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    public WebSecurity(
        @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
        RestAuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    /**
     * The main Security configuration
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .httpBasic()
                .disable()
            .exceptionHandling()
                // Return a 401 when a user try to access a protected resource
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
            .formLogin()
                .permitAll()
                // Return a 200 after a successful authentication
                .successHandler(successHandler())
                // Return a 401 after a failed authentication
                .failureHandler(failureHandler())
                .and()
            .authorizeRequests()
                // Allow access to the swagger
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/webjars/**","/swagger-resources/configuration/ui","/swagger-ui.html")
                    .permitAll()
                .antMatchers("/users/sign-up", "/users/reset-password", "/users/forgot-password", "/users/validate-email")
                    .permitAll()
                // Example of unprotected endpoint
                .antMatchers("/unprotected")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
            .logout()
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

    /**
     * We override the success handler to prevent Spring MVC from returning a 302 REDIRECT to the user application
     * We defer the login redirection handling to the front end.
     *
     * @return A success handler that will return a 200 on successful authentication
     */
    private AuthenticationSuccessHandler successHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());

        return successHandler;
    }

    /**
     * @return A simple failureHandler that returns a 401
     */
    private AuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    /**
     * We override the success handler to prevent Spring MVC from returning a 302 REDIRECT to the user application
     * We defer the logout redirection handling to the front end.
     *
     * @return A success handler that will return a 200 on successful logout
     */
    private LogoutSuccessHandler logoutSuccessHandler() {
        return new HttpStatusReturningLogoutSuccessHandler();
    }

    /**
     * Provide the authManager with a custom userDetailsService and our password encoder
     *
     * @param auth The auth manager builder
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * @see <a href="https://en.wikipedia.org/wiki/Bcrypt">https://en.wikipedia.org/wiki/Bcrypt</a>
     *
     * Provide a password encoder to the spring context for Dependency Injection
     * This password encoder is used to hash and compare password
     *
     * @return A BCrypt password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
