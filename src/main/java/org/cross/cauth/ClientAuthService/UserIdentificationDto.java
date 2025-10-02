package org.cross.cauth.ClientAuthService;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserIdentificationDto {
    @NotBlank(message = "auth code is required")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "appId is required")
    private String appId;
}
