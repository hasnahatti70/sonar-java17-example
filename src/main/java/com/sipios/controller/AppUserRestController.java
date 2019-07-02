package com.sipios.controller;

import com.sipios.model.user.AppUser;
import com.sipios.model.user.ResetPasswordForm;
import com.sipios.model.user.SignUpForm;
import com.sipios.model.user.ValidationEmailForm;
import com.sipios.model.user.token.ResetPasswordToken;
import com.sipios.model.user.token.ValidateEmailToken;
import com.sipios.repository.AppUserRepository;
import com.sipios.repository.ResetPasswordTokenRepository;
import com.sipios.repository.ValidateEmailTokenRepository;
import com.sipios.service.auth.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Api(tags = { "users", "auth" })
@AllArgsConstructor
public class AppUserRestController {

    private final AuthService authService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final ValidateEmailTokenRepository validateEmailTokenRepository;
    private final AppUserRepository userRepository;

    @PostMapping("/sign-up-no-email")
    @ApiOperation(value = "Sign-up", notes = "Sign-up a new user", tags = {"unprotected"})
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "", response = AppUser.class),
            @ApiResponse(code = 400, message = "Username is already taken", response = AppUser.class),
            @ApiResponse(code = 400, message = "Request body is not valid", response = AppUser.class),
        }
    )
    public ResponseEntity<AppUser> signUp2(@RequestBody @Valid AppUser user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/sign-up")
    @ApiOperation(value = "Sign-up", notes = "Sign-up a new user", tags = {"unprotected"})
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "", response = AppUser.class),
            @ApiResponse(code = 400, message = "Username is already taken", response = AppUser.class),
            @ApiResponse(code = 400, message = "Request body is not valid", response = AppUser.class),
        }
    )
    public ResponseEntity<AppUser> signUp(@RequestBody @Valid SignUpForm user) {
        return ResponseEntity.ok(authService.signUp(user));
    }

    @PostMapping("/forgot-password")
    @ApiOperation(value="Forgot password", notes = "Create a reset password token and send a reset password mail to the provided username", tags = {"unprotected"})
    @ApiResponses(
        {
            @ApiResponse(code = 202, message = "The reset token has been sent to user"),
            @ApiResponse(code = 202, message = "The user does not exists"),
            @ApiResponse(code = 400, message = "The request body is not valid"),
        }
    )
    public ResponseEntity<Void> resetPassword(
        @RequestBody @NotNull @ApiParam("The username of the user asking for his reset password token") String username
    ) {
        authService.sendResetPasswordToken(username);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/reset-password")
    @ApiOperation(value="Reset password", notes = "Change the user password of the token's corresponding user", tags = {"unprotected"})
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "The password has been updated"),
            @ApiResponse(code = 400, message = "The token is invalid"),
            @ApiResponse(code = 400, message = "The request body is not valid"),
        }
    )
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordForm resetPasswordForm) {
        Optional<ResetPasswordToken> optionalResetToken = resetPasswordTokenRepository.findById(resetPasswordForm.getResetToken());

        return optionalResetToken.map(resetToken -> {
            authService.resetUserPassword(resetPasswordForm, resetToken);

            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/validate-email")
    @ApiOperation(value="Validate email", notes = "Mark the user's email as validated", tags = {"unprotected"})
    @ApiResponses(
        {
            @ApiResponse(code = 200, message = "The email has been validated"),
            @ApiResponse(code = 400, message = "The token is invalid"),
            @ApiResponse(code = 400, message = "The request body is not valid"),
        }
    )
    public ResponseEntity<Object> validateEmail(@RequestBody @Valid ValidationEmailForm validateMailForm) {
        Optional<ValidateEmailToken> optionalResetToken = validateEmailTokenRepository.findById(validateMailForm.getToken());

        return optionalResetToken.map(token -> {
            authService.validateEmail(token);

            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.badRequest().build());
    }
}
