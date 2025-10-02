package org.cross.cauth.Application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationRequestDto {

    @NotBlank(message = "app name is required")
    private String appName;

    @NotBlank(message = "owner email is required")
    @Email(message = "enter valid email")
    private String ownerEmail;

    @NotBlank(message = "app secret is required")
    private String appSecret;

    private List<String> allowedCallbackUrls;
}
