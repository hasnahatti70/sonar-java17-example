package com.sipios.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The Application User
 */
@Data
@Entity
@ApiModel(value = "User", description = "A User representation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
        name = "uuid",
        strategy = "uuid2"
    )
    @ApiModelProperty(
        value = "An UUID used as a primary key to identify a user",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private String id;

    @ApiModelProperty(value = "The username that identify a user")
    private String username;

    @ApiModelProperty(value = "The password used for authentication purposes")
    @JsonIgnore
    private String password;

    @ApiModelProperty(
        value = "The DateTime at which the user validated his email",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private LocalDateTime mailVerifiedAt;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }
}
