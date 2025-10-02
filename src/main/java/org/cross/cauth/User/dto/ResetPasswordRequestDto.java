package org.cross.cauth.User.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResetPasswordRequestDto {

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 128, message = "New password must be between 8 and 128 characters")
    private String newPassword;

    public ResetPasswordRequestDto() {}

    public ResetPasswordRequestDto(String newPassword) {
        this.newPassword = newPassword;
    }


    @Override
    public String toString() {
        return "ResetPasswordRequestDto{" +
                "hasNewPassword=" + (newPassword != null && !newPassword.isEmpty()) +
                '}';
    }
}
