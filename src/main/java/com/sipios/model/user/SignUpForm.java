package com.sipios.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO Used during SignUp
 * Matches the field from the sign up form
 *
 * Validates the username field as an email
 */
@Data
@ApiModel(description = "Matches the fields from the sign up form")
public class SignUpForm extends PasswordForm {
    @NotNull
    @Email
    @NotBlank
    @Size(max = 255)
    @ApiModelProperty(value = "The username that identify a user")
    private String username;
}
