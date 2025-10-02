package org.cross.cauth.ClientAuthService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {

    @NotBlank(message = "auth id is required")
    private String authId;

    @NotBlank(message = "auth id is required")
    @Size(min = 6, max = 6, message = "security code must be 6 char long")
    private String securityCode;

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password must be at least 8 char long")
    private String newPassword;
}
