package com.sipios.repository;

import com.sipios.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "users", exported = false)
public interface AppUserRepository extends JpaRepository<AppUser, String>, QueryByExampleExecutor<AppUser> {
    AppUser findByUsername(@Param("username") String username);
}
