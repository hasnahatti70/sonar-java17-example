package com.sipios.utils;

import com.sipios.model.user.AppUser;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
public class WithMockBasicAuthSecurityContextFactory implements WithSecurityContextFactory<WithMockBasicAuth> {
    @Override
    public SecurityContext createSecurityContext(WithMockBasicAuth basicAuth) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (final String role : basicAuth.roles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        AppUser user = AppUser
            .builder()
            .username(basicAuth.principal())
            .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);
        context.setAuthentication(authentication);
        return context;
    }
}
