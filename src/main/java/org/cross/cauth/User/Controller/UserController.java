package org.cross.cauth.User.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cross.cauth.ApiResponse;
import org.cross.cauth.User.Service.UserService;
import org.cross.cauth.User.dto.*;
import org.cross.cauth.User.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        ApiResponse<List<UserResponseDto>> response = ApiResponse.<List<UserResponseDto>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/{authId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByAuthId(@PathVariable String authId) {
        UserResponseDto user = userService.getUserByAuthId(authId);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable UUID id) {
        UserResponseDto user = userService.getUserById(id);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByEmailAndAppId(
            @RequestParam String email,
            @RequestParam String appId) {
        UserResponseDto user = userService.getUserByEmailAndAppId(email, appId);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody CreateUserRequestDto createUserDto) {
        UserResponseDto user = UserMapper.toResponseDto(userService.createUser(createUserDto));
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User created successfully")
                .timestamp(LocalDateTime.now())
                .data(user)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/auth/{authId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable String authId,
            @Valid @RequestBody UpdateUserRequestDto updateUserDto) {
        UserResponseDto user = userService.updateUser(authId, updateUserDto);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User updated successfully")
                .timestamp(LocalDateTime.now())
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/auth/{authId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String authId) {
        userService.deleteUser(authId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<UserResponseDto>> authenticateUser(@Valid @RequestBody UserLoginRequestDto loginDto) {
        UserResponseDto user = userService.authenticateUser(loginDto);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("User authenticated successfully")
                .timestamp(LocalDateTime.now())
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/auth/{authId}/verify-email")
    public ResponseEntity<ApiResponse<UserResponseDto>> verifyEmail(@PathVariable String authId) {
        UserResponseDto user = userService.verifyEmail(authId);
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("Email verified successfully")
                .timestamp(LocalDateTime.now())
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> userExists(
            @RequestParam String email,
            @RequestParam String appId) {
        boolean exists = userService.userExists(email, appId);
        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .success(true)
                .message("User existence check completed")
                .timestamp(LocalDateTime.now())
                .data(exists)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/app/{appId}")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUsersByAppId(@PathVariable String appId) {
        List<UserResponseDto> users = userService.getUsersByAppId(appId);
        ApiResponse<List<UserResponseDto>> response = ApiResponse.<List<UserResponseDto>>builder()
                .success(true)
                .message("Users retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unverified")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getUnverifiedUsers() {
        List<UserResponseDto> users = userService.getUnverifiedUsers();
        ApiResponse<List<UserResponseDto>> response = ApiResponse.<List<UserResponseDto>>builder()
                .success(true)
                .message("Unverified users retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/auth/{authId}/change-password")
    public ResponseEntity<ApiResponse<UserResponseDto>> changePassword(
            @PathVariable String authId,
            @Valid @RequestBody ChangePasswordRequestDto changePasswordDto) {
        UserResponseDto user = userService.changePassword(
                authId,
                changePasswordDto.getCurrentPassword(),
                changePasswordDto.getNewPassword()
        );
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("Password changed successfully")
                .timestamp(LocalDateTime.now())
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/auth/{authId}/reset-password")
    public ResponseEntity<ApiResponse<UserResponseDto>> resetPassword(
            @PathVariable String authId,
            @RequestBody ResetPasswordRequestDto resetPasswordDto) {
        UserResponseDto user = userService.resetPassword(authId, resetPasswordDto.getNewPassword());
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .success(true)
                .message("Password reset successfully")
                .timestamp(LocalDateTime.now())
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getUserCount() {
        long count = userService.getUserCount();
        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .success(true)
                .message("User count retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/app/{appId}")
    public ResponseEntity<ApiResponse<Long>> getUserCountByAppId(@PathVariable String appId) {
        long count = userService.getUserCountByAppId(appId);
        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .success(true)
                .message("User count retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(count)
                .build();
        return ResponseEntity.ok(response);
    }
}
