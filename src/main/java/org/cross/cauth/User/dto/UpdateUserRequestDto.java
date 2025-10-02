package org.cross.cauth.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserRequestDto {

    /**
     * User's email address - optional for updates
     */
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    /**
     * Phone number - optional for updates
     */
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    /**
     * New password - optional for updates
     */
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;

    // Constructors
    public UpdateUserRequestDto() {}

    public UpdateUserRequestDto(String email) {
        this.email = email;
    }

    public UpdateUserRequestDto(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UpdateUserRequestDto(String email, String phoneNumber, String password) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }


    // Utility methods
    public boolean hasEmail() {
        return email != null && !email.trim().isEmpty();
    }

    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.trim().isEmpty();
    }

    public boolean hasPassword() {
        return password != null && !password.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "UpdateUserRequestDto{" +
                "email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", hasPassword=" + hasPassword() +
                '}';
    }
}
