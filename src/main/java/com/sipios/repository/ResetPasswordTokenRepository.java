package com.sipios.repository;

import com.sipios.model.user.token.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "reset-password-token", exported = false)
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String>, QueryByExampleExecutor<ResetPasswordToken> {
}
