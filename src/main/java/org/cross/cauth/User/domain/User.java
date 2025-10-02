package org.cross.cauth.User.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * User entity representing authenticated users in the system.
 * Contains essential fields for authentication and user management.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "auth_id", nullable = false, unique = true, length = 50)
    private String authId;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "app_id", nullable = false, length = 255)
    private String appId;

    @Column(name = "hashed_password", nullable = false, length = 255)
    private String hashedPassword;

    @Column(name = "is_email_verified", nullable = false)
    private Boolean isEmailVerified = false;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /**
     * Account creation timestamp
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Last update timestamp
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "role")
    private String role;

    // Constructors
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isEmailVerified = false;
        this.role = "ROLE_USER";
    }


    // JPA lifecycle callbacks
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Utility methods
    public boolean isEmailVerified() {
        return Boolean.TRUE.equals(this.isEmailVerified);
    }

    public void verifyEmail() {
        this.isEmailVerified = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.trim().isEmpty();
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(authId, user.authId) &&
               Objects.equals(email, user.email) &&
               Objects.equals(appId, user.appId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authId, email, appId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", authId='" + authId + '\'' +
                ", email='" + email + '\'' +
                ", appId='" + appId + '\'' +
                ", isEmailVerified=" + isEmailVerified +
                ", hasPhoneNumber=" + hasPhoneNumber() +
                ", createdAt=" + createdAt +
                '}';
    }
}
