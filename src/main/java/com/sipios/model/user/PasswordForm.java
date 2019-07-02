package com.sipios.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
public class PasswordForm {
    /**
     * Password size is bigger than 10
     * Password must meet at least 3 out of the following 4 complexity rules
     * - at least 1 uppercase character (A-Z)
     * - at least 1 lowercase character (a-z)
     * - at least 1 digit (0-9)
     * - at least 1 special character (punctuation)
     *
     * @see <a href="https://github.com/OWASP/CheatSheetSeries/blob/master/cheatsheets/Authentication_Cheat_Sheet.md">https://github.com/OWASP/CheatSheetSeries/blob/master/cheatsheets/Authentication_Cheat_Sheet.md</a>
     */
    @ApiModelProperty(value = "A plain text password")
    @Pattern(regexp = "^(?:(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[^A-Za-z0-9])(?=.*[a-z])|(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))(?!.*(.)\\1{2,})[A-Za-z0-9!~<>,;:_=?*+#.\"&§%°()|\\[\\]\\-$^@/]{10,255}$")
    private String password;
}
