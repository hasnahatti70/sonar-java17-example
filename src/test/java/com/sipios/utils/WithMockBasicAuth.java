package com.sipios.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockBasicAuthSecurityContextFactory.class)
public @interface WithMockBasicAuth {
    String principal();
    String[] roles() default {};
}
