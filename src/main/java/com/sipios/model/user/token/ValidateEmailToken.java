package com.sipios.model.user.token;

import com.sipios.model.user.AppUser;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@ApiModel(description = "A token representation to manage user email validation")
@Entity
@NoArgsConstructor
// Where clause added to queries to apply a soft delete
@Where(clause = "current_timestamp() < expiry_date and used_date is null")
public class ValidateEmailToken extends Token {

    @Builder
    private ValidateEmailToken(String id, LocalDateTime expiryDate, LocalDateTime usedDate, AppUser appUser) {
        super(id, expiryDate, usedDate, appUser);
    }
}
