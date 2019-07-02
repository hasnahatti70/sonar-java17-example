package com.sipios.service.auth;

import com.sipios.config.ApplicationProperties;
import com.sipios.model.user.AppUser;
import com.sipios.model.user.ResetPasswordForm;
import com.sipios.model.user.SignUpForm;
import com.sipios.model.user.token.ResetPasswordToken;
import com.sipios.model.user.token.ValidateEmailToken;
import com.sipios.repository.AppUserRepository;
import com.sipios.repository.ResetPasswordTokenRepository;
import com.sipios.repository.ValidateEmailTokenRepository;
import com.sipios.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/**
 * Service that handles operations relating to the user authentication
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ValidateEmailTokenRepository emailTokenRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final ApplicationProperties applicationProperties;

    /**
     * Creates a new user if it does not exist and send him a validation email
     *
     * @param user The user to create
     * @return The created user
     */
    public AppUser signUp(SignUpForm user) {
        AppUser appUser = userRepository.findByUsername(user.getUsername());
        if (appUser != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User could not be created");
        }

        AppUser createdUser = userRepository.save(
            AppUser
                .builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .build()
        );

        LocalDateTime expiryDate = LocalDateTime.now().plus(this.applicationProperties.getResetPassword().getResetTokenDuration());
        ValidateEmailToken token = emailTokenRepository.save(
            ValidateEmailToken.builder()
                .appUser(createdUser)
                .expiryDate(expiryDate)
                .build()
        );

        emailService.sendValidationEmail(createdUser, token);

        return createdUser;
    }

    /**
     * Creates and send a reset password token to the user if it does exist
     *
     * @param username A username corresponding to an user
     */
    public void sendResetPasswordToken(String username) {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            return;
        }

        LocalDateTime expiryDate = LocalDateTime.now().plus(this.applicationProperties.getResetPassword().getResetTokenDuration());
        ResetPasswordToken token = resetPasswordTokenRepository.save(
            ResetPasswordToken.builder()
                .appUser(appUser)
                .expiryDate(expiryDate)
                .build()
        );

        emailService.sendResetPassword(appUser, token);
    }

    /**
     * Changes the password of the requested user and mark the reset password token as used
     *
     * @param resetPasswordForm The reset password form
     * @param resetToken The reset token
     */
    public void resetUserPassword(ResetPasswordForm resetPasswordForm, ResetPasswordToken resetToken) {
        AppUser user = resetToken.getAppUser();
        user.setPassword(passwordEncoder.encode(resetPasswordForm.getPassword()));
        userRepository.save(user);

        resetToken.setUsedDate(LocalDateTime.now());
        resetPasswordTokenRepository.save(resetToken);
    }

    /**
     * Marks the user as verified
     *
     * @param token The email validation token
     */
    public void validateEmail(ValidateEmailToken token) {
        AppUser user = token.getAppUser();
        user.setMailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        token.setUsedDate(LocalDateTime.now());
        emailTokenRepository.save(token);
    }
}
