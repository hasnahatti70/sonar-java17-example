package com.sipios.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO Used during mail validation
 */
@Data
@ApiModel(description = "Contain the mail validation token that has been sent to the user by mail")
public class ValidationEmailForm {
    @NotBlank
    @Size(max = 255)
    @ApiModelProperty(value = "The id of the token that was sent to the user by mail")
    private String token;
}
