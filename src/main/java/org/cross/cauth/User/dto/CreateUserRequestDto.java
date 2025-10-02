package org.cross.cauth.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateUserRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    /**
     * Application ID - required to identify which appId the user belongs to
     */
    @NotBlank(message = "Application ID is required")
    @Size(max = 50, message = "Application ID must not exceed 50 characters")
    private String appId;

    /**
     * Plain text password - will be hashed before storage
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    // Constructors
    public CreateUserRequestDto() {}

    public CreateUserRequestDto(String email, String appId, String password) {
        this.email = email;
        this.appId = appId;
        this.password = password;
    }

    public CreateUserRequestDto(String email, String appId, String password, String phoneNumber) {
        this.email = email;
        this.appId = appId;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "CreateUserRequestDto{" +
                "email='" + email + '\'' +
                ", appId='" + appId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
