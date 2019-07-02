package com.sipios.controller;

import com.sipios.IntegrationTest;
import com.sipios.model.user.AppUser;
import com.sipios.model.user.token.ResetPasswordToken;
import com.sipios.model.user.token.ValidateEmailToken;
import com.sipios.repository.AppUserRepository;
import com.sipios.repository.ResetPasswordTokenRepository;
import com.sipios.repository.ValidateEmailTokenRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AppUserRestControllerIntTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ResetPasswordTokenRepository passwordTokenRepository;

    @Autowired
    private ValidateEmailTokenRepository emailTokenRepository;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void test_sign_up_login_logout_process() throws Exception {
        // Sign up
        mockMvc.perform(post("/users/sign-up")
            .content("{ \"username\": \"admin@test.com\", \"password\": \"password1@\" }")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // User exists
        AppUser user = userRepository.findByUsername("admin@test.com");
        Assert.assertNotNull(user);
        Assert.assertNull(user.getMailVerifiedAt());

        // Email sent
        List<ValidateEmailToken> tokens = emailTokenRepository.findAll(Example.of(ValidateEmailToken.builder().appUser(user).build()));
        Assert.assertEquals(1, tokens.size());
        ValidateEmailToken token = tokens.get(0);

        // Login
        Cookie session = mockMvc.perform(post("/login")
            .content("username=admin%40test.com&password=password1@")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andReturn().getResponse()
            .getCookie("SESSION");

        // Access protected resource
        mockMvc.perform(get("/protected").cookie(session)).andExpect(status().isOk());

        // Validate Email
        mockMvc.perform(
            post("/users/validate-email")
                .content("{\n" +
                    "  \"token\": \""+ token.getId() +"\"\n" +
                    "}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        ValidateEmailToken usedToken = emailTokenRepository.getOne(token.getId());
        Assert.assertNotNull(usedToken.getUsedDate());
        user = userRepository.findByUsername("admin@test.com");
        Assert.assertNotNull(user.getMailVerifiedAt());

        // Logout
        mockMvc.perform(post("/logout").cookie(session)).andExpect(status().isOk());

        // Access protected resource with failure
        mockMvc.perform(get("/protected").cookie(session)).andExpect(status().isUnauthorized());
    }

    @Test
    public void test_sign_up_existing_user() throws Exception {
        mockMvc.perform(post("/users/sign-up")
            .content("{ \"username\": \"admin@test.com\", \"password\": \"password1@\" }")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(post("/users/sign-up")
            .content("{ \"username\": \"admin@test.com\", \"password\": \"password1@\" }")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void test_login_wrong_username() throws Exception {
        mockMvc.perform(post("/login")
            .content("username=admin%40test.com&password=password")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void test_reset_password_process() throws Exception {
        // With existing user
        AppUser user = userRepository.save(AppUser.builder().username("reset-password@test.com").build());

        mockMvc.perform(
            post("/users/forgot-password")
                .content("reset-password@test.com")
                .contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isAccepted());

        List<ResetPasswordToken> tokens = passwordTokenRepository.findAll(Example.of(ResetPasswordToken.builder().appUser(user).build()));
        Assert.assertEquals(1, tokens.size());
        ResetPasswordToken token = tokens.get(0);

        // Change password
        mockMvc.perform(
            post("/users/reset-password")
                .content("{\n" +
                    "  \"password\": \"testtest1@\",\n" +
                    "  \"resetToken\": \""+ token.getId() +"\"\n" +
                    "}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        ResetPasswordToken usedToken = passwordTokenRepository.getOne(token.getId());
        Assert.assertNotNull(usedToken.getUsedDate());
    }

    @Test
    public void test_reset_password_hides_email() throws Exception {
        mockMvc.perform(
            post("/users/forgot-password")
                .content("unexisting@test.com")
                .contentType(MediaType.TEXT_PLAIN)
        ).andExpect(status().isAccepted());
    }

    @Test
    public void test_cannot_reuse_reset_password_token() throws Exception {
        ResetPasswordToken token = passwordTokenRepository.saveAndFlush(ResetPasswordToken.builder().usedDate(LocalDateTime.now()).build());
        // Clear cache to trigger a new SQL Request
        entityManager.clear();

        mockMvc.perform(
            post("/users/reset-password")
                .content("{\n" +
                    "  \"password\": \"testtest@1\",\n" +
                    "  \"resetToken\": \""+ token.getId() +"\"\n" +
                    "}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void test_cannot_reuse_validate_email_token() throws Exception {
        AppUser user = userRepository.save(AppUser.builder().username("validate-email@test.com").build());
        ValidateEmailToken token = emailTokenRepository.saveAndFlush(
            ValidateEmailToken.builder()
                .usedDate(LocalDateTime.now())
                .appUser(user)
                .build()
        );
        // Clear cache to trigger a new SQL Request
        entityManager.clear();

        mockMvc.perform(
            post("/users/validate-email")
                .content("{\n" +
                    "  \"token\": \""+ token.getId() +"\"\n" +
                    "}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}
