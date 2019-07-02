package com.sipios.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Example of a protected resource
 */
@RestController
@RequestMapping("protected")
@Api(tags = { "protected" })
public class ProtectedRestController {
    /**
     * This resource is protected by the implementation of the WebSecurity configuration
     * @see com.sipios.security.WebSecurity
     *
     * An example of authorization anotation @PreAuthorize if also used
     * @return
     */
    @GetMapping
    @PreAuthorize("@securityConfig.isRegistered(authentication)")
    @ApiOperation(value = "Hello World", notes = "A resource that returns hello world")
    public ResponseEntity<String> protectedHelloWorld() {
        return ResponseEntity.ok("Hello, world!");
    }
}
