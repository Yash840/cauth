package org.cross.cauth.ClientAuthService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TokenRequestDto {
    @NotBlank(message = "auth code is required")
    @Size(min = 12, max = 12, message = "auth code must be 12 character long")
    private String authCode;

    @NotBlank(message = "appId is required")
    private String appId;

    @NotBlank(message = "app secret is required")
    private String appSecret;
}
