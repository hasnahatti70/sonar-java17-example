package com.sipios.controller;

import com.sipios.model.user.LoginForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication API specification for Swagger documentation and Code Generation.
 * Implemented by Spring Security.
 * This class is not directly used. It's implementation is overridden by Spring Security
 */
@Api(tags = {"auth"})
@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthApi {
    /**
     * Implemented by Spring Security
     * @param user
     * @return token
     */
    @ApiOperation(value = "Login", notes = "Login with the given credentials.", tags = {"unprotected"})
    @ApiResponses({@ApiResponse(code = 200, message = "", response = String.class)})
    @PostMapping(value = "/login", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String login(
        @RequestBody LoginForm user
    ) {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }

    /**
     * Implemented by Spring Security
     */
    @ApiOperation(value = "Logout", notes = "Logout the current user.", tags = {"protected"})
    @ApiResponses({@ApiResponse(code = 200, message = "")})
    @PostMapping(value = "/logout")
    public void logout() {
        throw new IllegalStateException("Add Spring Security to handle authentication");
    }
}
