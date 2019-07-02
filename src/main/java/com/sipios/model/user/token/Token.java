package com.sipios.model.user.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sipios.model.user.AppUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base token class that describes a token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Token implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(
        name = "uuid",
        strategy = "uuid2"
    )
    @ApiModelProperty(
        value = "An UUID used as a primary key to identify a Token",
        accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private String id;

    @ApiModelProperty(value = "The date after which the token expires")
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @ApiModelProperty(value = "The date at which the reset token was used")
    @Column(name = "used_date")
    private LocalDateTime usedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private AppUser appUser;
}
