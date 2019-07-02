package com.sipios.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO Used during Login
 * Matches the fields from the login form
 */
@Data
@ApiModel(description = "Matches the fields from the login form")
public class LoginForm {
    @NotNull
    @NotBlank
    @Email
    @ApiModelProperty(value = "The username that identify a user")
    private String username;

    @NotNull
    @NotBlank
    @ApiModelProperty(value = "The user plain text password")
    private String password;
}
