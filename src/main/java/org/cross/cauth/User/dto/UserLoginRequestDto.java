package org.cross.cauth.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserLoginRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotBlank(message = "Application ID is required")
    @Size(max = 50, message = "Application ID must not exceed 50 characters")
    private String appId;

    @NotBlank(message = "Password is required")
    private String password;

    // Constructors
    public UserLoginRequestDto() {}

    public UserLoginRequestDto(String email, String appId, String password) {
        this.email = email;
        this.appId = appId;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "email='" + email + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}
