package org.cross.cauth.User.Service;

import lombok.RequiredArgsConstructor;
import org.cross.cauth.Application.Repository.ApplicationRepository;
import org.cross.cauth.Application.domain.Application;
import org.cross.cauth.Exception.ApplicationNotFoundException;
import org.cross.cauth.Exception.InvalidCredentialsException;
import org.cross.cauth.Exception.UserAlreadyExistsException;
import org.cross.cauth.Exception.UserNotFoundException;
import org.cross.cauth.User.domain.User;
import org.cross.cauth.User.dto.CreateUserRequestDto;
import org.cross.cauth.User.dto.UpdateUserRequestDto;
import org.cross.cauth.User.dto.UserLoginRequestDto;
import org.cross.cauth.User.dto.UserResponseDto;
import org.cross.cauth.User.mapper.UserMapper;
import org.cross.cauth.User.repository.UserRepository;
import org.cross.cauth.utils.CredentialSecurityManager;
import org.cross.cauth.utils.UniqueIdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final CredentialSecurityManager passwordEncoder;
    private final UserMapper userMapper;
    private final UniqueIdGenerator appIdGenerator;

    /**
     * Get all users in the system
     * @return List of all users as response DTOs
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }

    /**
     * Get user by authentication ID
     * @param authId The user's authentication ID
     * @return UserResponseDto
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserByAuthId(String authId) {
        return UserMapper.toResponseDto(
                userRepository.findByAuthId(authId)
                        .orElseThrow(() -> new UserNotFoundException("User not found with auth ID: " + authId))
        );
    }

    /**
     * Get user by ID
     * @param id The user's UUID
     * @return UserResponseDto
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID id) {
        return UserMapper.toResponseDto(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id))
        );
    }

    /**
     * Get user by email and appId ID
     * @param email The user's email
     * @param appId The appId ID
     * @return UserResponseDto
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmailAndAppId(String email, String appId) {
        User user = userRepository.findByEmailAndAppId(email, appId)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email + " in app: " + appId));
        return UserMapper.toResponseDto(user);
    }

    @Transactional(readOnly = true)
    public String identifyUserByEmailAndAppId(String email, String appId) {
        return getUserByEmailAndAppId(email, appId).getAuthId();
    }

    /**
     * Create a new user
     * @param createUserDto The user creation request
     * @return User object of the created user
     * @throws UserAlreadyExistsException if user already exists
     */
    public User createUser(CreateUserRequestDto createUserDto) {
        // Validate appId exists
        Application application = applicationRepository.findByAppId(createUserDto.getAppId())
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found: " + createUserDto.getAppId()));

        // Check if user already exists with this email in this appId
        if (userRepository.findByEmailAndAppId(createUserDto.getEmail(), createUserDto.getAppId()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + createUserDto.getEmail() + " in appId: " + createUserDto.getAppId());
        }

        // Generate unique auth ID
        String authId = generateUniqueAuthId();

        // Hash password
        String hashedPassword = passwordEncoder.encode(createUserDto.getPassword());

        // Create user entity
        User user = UserMapper.toEntity(createUserDto, application, authId, hashedPassword);
        user.setRole("ROLE_USER");

        // Save user

        return userRepository.save(user);
    }

    /**
     * Verifies password with password stored in db
     * @param user The User object whose password is to be checked
     * @param enteredPassword The password entered by log in entity
     * @return <code>true</code> if password is correct otherwise <code>false</code>
     */
    public boolean verifyUserPassword(User user, String enteredPassword){
        return passwordEncoder.isValid(
                enteredPassword,
                user.getHashedPassword()
        );
    }

    /**
     *Validates user credentials by checking if user exists and password is correct
     * @param email The user's email address
     * @param appId The application ID the user belongs to
     * @param password The password to verify
     * @return <code>true</code> if user exists and password is valid, <code>false</code> otherwise
     * @throws UserNotFoundException if user with given email and appId does not exist
     */
    public boolean isValidUser(String email, String appId, String password){
        User user = userRepository.findByEmailAndAppId(email, appId)
                .orElseThrow(() -> new UserNotFoundException("user does not exist"));

        return verifyUserPassword(user, password);
    }

    /**
     * Update user information
     * @param authId The user's auth ID
     * @param updateUserDto The update request
     * @return Updated UserResponseDto
     * @throws UserNotFoundException if user not found
     */
    public UserResponseDto updateUser(String authId, UpdateUserRequestDto updateUserDto) {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new UserNotFoundException("User not found with auth ID: " + authId));

        // Check if email is being changed and if it conflicts with existing users
        if (updateUserDto.hasEmail() && !updateUserDto.getEmail().equals(user.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmailAndAppId(updateUserDto.getEmail(), user.getAppId());
            if (existingUser.isPresent() && !existingUser.get().getAuthId().equals(authId)) {
                throw new UserAlreadyExistsException("Email already exists in this appId: " + updateUserDto.getEmail());
            }
        }

        // Hash new password if provided
        String hashedPassword = null;
        if (updateUserDto.hasPassword()) {
            hashedPassword = passwordEncoder.encode(updateUserDto.getPassword());
        }

        // Update user entity
        UserMapper.updateEntityFromDto(user, updateUserDto, hashedPassword);

        // Save updated user
        User updatedUser = userRepository.save(user);

        return UserMapper.toResponseDto(updatedUser);
    }

    /**
     * Delete user by auth ID
     * @param authId The user's auth ID
     * @throws UserNotFoundException if user not found
     */
    public void deleteUser(String authId) {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new UserNotFoundException("User not found with auth ID: " + authId));

        userRepository.delete(user);
    }

    /**
     * Authenticate user with email and password
     * @param loginDto The login request
     * @return UserResponseDto if authentication successful
     * @throws UserNotFoundException if user not found or password invalid
     */
    @Transactional(readOnly = true)
    public UserResponseDto authenticateUser(UserLoginRequestDto loginDto) {
        User user = userRepository.findByEmailAndAppId(loginDto.getEmail(), loginDto.getAppId())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.isValid(loginDto.getPassword(), user.getHashedPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return UserMapper.toResponseDto(user);
    }

    /**
     * Verify user's email
     * @param authId The user's auth ID
     * @return Updated UserResponseDto
     * @throws UserNotFoundException if user not found
     */
    public UserResponseDto verifyEmail(String authId) {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new UserNotFoundException("User not found with auth ID: " + authId));

        user.verifyEmail();
        User updatedUser = userRepository.save(user);

        return UserMapper.toResponseDto(updatedUser);
    }

    /**
     * Check if user exists by email and app ID
     * @param email The user's email
     * @param appId The appId ID
     * @return true if user exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean userExists(String email, String appId) {
        return userRepository.findByEmailAndAppId(email, appId).isPresent();
    }

    /**
     * Get all users for a specific appId
     * @param appId The appId ID
     * @return List of users in the appId
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByAppId(String appId) {
        // Validate appId exists
        applicationRepository.findByAppId(appId)
                .orElseThrow(() -> new UserNotFoundException("Application not found: " + appId));

        return userRepository.findByAppId(appId)
                .stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }

    /**
     * Get all unverified users
     * @return List of unverified users
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUnverifiedUsers() {
        return userRepository.findByIsEmailVerified(false)
                .stream()
                .map(UserMapper::toResponseDto)
                .toList();
    }

    /**
     * Change user password
     * @param authId The user's auth ID
     * @param currentPassword The current password
     * @param newPassword The new password
     * @return Updated UserResponseDto
     * @throws UserNotFoundException if user not found or current password invalid
     */
    public UserResponseDto changePassword(String authId, String currentPassword, String newPassword) {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new UserNotFoundException("User not found with auth ID: " + authId));

        // Verify current password
        if (!passwordEncoder.isValid(currentPassword, user.getHashedPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        // Hash and set new password
        String hashedNewPassword = passwordEncoder.encode(newPassword);
        user.setHashedPassword(hashedNewPassword);

        User updatedUser = userRepository.save(user);
        return UserMapper.toResponseDto(updatedUser);
    }

    /**
     * Reset user password (admin function)
     * @param authId The user's auth ID
     * @param newPassword The new password
     * @return Updated UserResponseDto
     * @throws UserNotFoundException if user not found
     */
    public UserResponseDto resetPassword(String authId, String newPassword) {
        User user = userRepository.findByAuthId(authId)
                .orElseThrow(() -> new UserNotFoundException("User not found with auth ID: " + authId));

        // Hash and set new password
        String hashedNewPassword = passwordEncoder.encode(newPassword);
        user.setHashedPassword(hashedNewPassword);

        User updatedUser = userRepository.save(user);
        return UserMapper.toResponseDto(updatedUser);
    }

    /**
     * Get user count for statistics
     * @return Total number of users
     */
    @Transactional(readOnly = true)
    public long getUserCount() {
        return userRepository.count();
    }

    /**
     * Get user count by appId
     * @param appId The appId ID
     * @return Number of users in the appId
     */
    @Transactional(readOnly = true)
    public long getUserCountByAppId(String appId) {
        return userRepository.countByAppId(appId);
    }

    /**
     * Get user object by email and appId
     * @param email The email of user
     * @param appId The appId user associated with
     * @return User Object
     */
    public User getUserEntityByEmailAndAppId(String email, String appId){
        return userRepository.findByEmailAndAppId(email, appId)
                .orElseThrow(() -> new UserNotFoundException("Invalid email or app id"));
    }

    /**
     * Generate a unique authentication ID for new users
     * @return Unique auth ID
     */
    private String generateUniqueAuthId() {
        String authId;
        do {
            authId = appIdGenerator.generate().replace("APP.", "user.");
        } while (userRepository.findByAuthId(authId).isPresent());

        return authId;
    }
}
