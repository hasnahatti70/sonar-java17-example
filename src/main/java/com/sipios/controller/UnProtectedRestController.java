package com.sipios.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Example of unprotected resource
 * @see com.sipios.security.WebSecurity#configure(HttpSecurity)}
 */
@RestController
@RequestMapping("unprotected")
@Api(tags = "unprotected")
public class UnProtectedRestController {
    @GetMapping
    @ApiOperation(value = "Hello World", notes = "A resource that returns hello world")
    public ResponseEntity<String> unProtectedHelloWorld() {
        return ResponseEntity.ok("Hello, world!");
    }
}
