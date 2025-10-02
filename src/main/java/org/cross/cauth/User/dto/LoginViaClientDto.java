package org.cross.cauth.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginViaClientDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    // Constructors
    public LoginViaClientDto() {}

    public LoginViaClientDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequestDto{" +
                "email='" + email + '\'' +
                '}';
    }
}
