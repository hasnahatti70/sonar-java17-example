package com.sipios.repository;

import com.sipios.model.user.token.ValidateEmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "valid-email-token", exported = false)
public interface ValidateEmailTokenRepository extends JpaRepository<ValidateEmailToken, String>, QueryByExampleExecutor<ValidateEmailToken> {
}
