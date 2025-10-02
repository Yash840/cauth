package org.cross.cauth.User.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for password change requests.
 * Contains current password and new password for secure password updates.
 */
@Builder
@Getter
@Setter
public class ChangePasswordRequestDto {

    /**
     * Current password - required for verification
     */
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    /**
     * New password - must meet security requirements
     */
    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 128, message = "New password must be between 8 and 128 characters")
    private String newPassword;

    // Constructors
    public ChangePasswordRequestDto() {}

    public ChangePasswordRequestDto(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordRequestDto{" +
                "hasCurrentPassword=" + (currentPassword != null && !currentPassword.isEmpty()) +
                ", hasNewPassword=" + (newPassword != null && !newPassword.isEmpty()) +
                '}';
    }
}
