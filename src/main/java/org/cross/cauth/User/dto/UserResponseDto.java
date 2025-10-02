package org.cross.cauth.User.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class UserResponseDto {

    private UUID id;

    private String authId;

    private String email;

    private String appId;

    private Boolean isEmailVerified;

    private String phoneNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String role;

    // Constructors
    public UserResponseDto() {}

    public UserResponseDto(UUID id, String authId, String email, String appId,
                          Boolean isEmailVerified, String phoneNumber,
                          LocalDateTime createdAt, LocalDateTime updatedAt, String role) {
        this.id = id;
        this.authId = authId;
        this.email = email;
        this.appId = appId;
        this.isEmailVerified = isEmailVerified;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
    }

    // Utility methods
    public boolean isEmailVerified() {
        return Boolean.TRUE.equals(this.isEmailVerified);
    }

    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "id=" + id +
                ", authId='" + authId + '\'' +
                ", email='" + email + '\'' +
                ", appId='" + appId + '\'' +
                ", isEmailVerified=" + isEmailVerified +
                ", hasPhoneNumber=" + hasPhoneNumber() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
