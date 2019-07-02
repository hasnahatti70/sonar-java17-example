package com.sipios.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "Matches the fields from the reset password form")
public class ResetPasswordForm extends PasswordForm {
    @NotNull
    @NotBlank
    @ApiModelProperty(value = "The id of the token that was sent to the user by mail")
    private String resetToken;
}
