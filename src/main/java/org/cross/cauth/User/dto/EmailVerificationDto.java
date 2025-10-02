package org.cross.cauth.User.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EmailVerificationDto {

    @NotBlank(message = "Auth ID is required")
    @Size(max = 50, message = "Auth ID must not exceed 50 characters")
    private String authId;


    @NotBlank(message = "Verification token is required")
    @Size(max = 255, message = "Verification token must not exceed 255 characters")
    private String verificationToken;

    // Constructors
    public EmailVerificationDto() {}

    public EmailVerificationDto(String authId, String verificationToken) {
        this.authId = authId;
        this.verificationToken = verificationToken;
    }

    @Override
    public String toString() {
        return "EmailVerificationDto{" +
                "authId='" + authId + '\'' +
                ", hasToken=" + (verificationToken != null && !verificationToken.isEmpty()) +
                '}';
    }
}
