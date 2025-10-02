package org.cross.cauth.User.mapper;

import org.cross.cauth.Application.domain.Application;
import org.cross.cauth.User.domain.User;
import org.cross.cauth.User.dto.CreateUserRequestDto;
import org.cross.cauth.User.dto.UpdateUserRequestDto;
import org.cross.cauth.User.dto.UserResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between User entity and DTOs.
 * Handles the mapping logic for user-related data transformations.
 */
@Component
public class UserMapper {

    /**
     * Converts CreateUserRequestDto to User entity.
     * Note: Password should be hashed and authId should be generated before calling this method.
     *
     * @param dto The create user request DTO
     * @param application The appId entity the user belongs to
     * @param authId The generated unique auth ID
     * @param hashedPassword The hashed password
     * @return User entity
     */
    public static User toEntity(CreateUserRequestDto dto, Application application, String authId, String hashedPassword) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setAuthId(authId);
        user.setEmail(dto.getEmail());
        user.setAppId(application.getAppId());
        user.setHashedPassword(hashedPassword);
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setIsEmailVerified(false); // Default to false for new users
        user.setRole("ROLE_USER");

        return user;
    }

    /**
     * Converts User entity to UserResponseDto.
     *
     * @param user The user entity
     * @return UserResponseDto
     */
    public static UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDto(
            user.getId(),
            user.getAuthId(),
            user.getEmail(),
            user.getAppId() != null ? user.getAppId() : null,
            user.getIsEmailVerified(),
            user.getPhoneNumber(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
                user.getRole()
        );
    }

    /**
     * Updates User entity with data from UpdateUserRequestDto.
     * Only updates non-null fields from the DTO.
     *
     * @param user The existing user entity to update
     * @param dto The update request DTO
     * @param hashedPassword The new hashed password (if password was provided)
     */
    public static void updateEntityFromDto(User user, UpdateUserRequestDto dto, String hashedPassword) {
        if (user == null || dto == null) {
            return;
        }

        if (dto.hasEmail()) {
            user.setEmail(dto.getEmail());
            // Reset email verification when email is changed
            user.setIsEmailVerified(false);
        }

        if (dto.hasPhoneNumber()) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.hasPassword() && hashedPassword != null) {
            user.setHashedPassword(hashedPassword);
        }
    }

    /**
     * Creates a simplified UserResponseDto for public API responses.
     * Excludes sensitive information like authId.
     *
     * @param user The user entity
     * @return Simplified UserResponseDto
     */
    public static UserResponseDto toPublicResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = toResponseDto(user);
        // Remove sensitive information for public responses
        dto.setAuthId(null);

        return dto;
    }
}
