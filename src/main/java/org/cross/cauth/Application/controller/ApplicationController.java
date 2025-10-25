package org.cross.cauth.Application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cross.cauth.ApiResponse;
import org.cross.cauth.Application.Service.ApplicationService;
import org.cross.cauth.Application.dto.ApplicationRequestDto;
import org.cross.cauth.Application.dto.ApplicationResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/apps")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getAllApplications() {
        List<ApplicationResponseDto> applications = applicationService.getAllApplications();
        ApiResponse<List<ApplicationResponseDto>> response = ApiResponse.<List<ApplicationResponseDto>>builder()
                .success(true)
                .message("Applications retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(applications)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{appId}")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> getApplicationByAppId(@PathVariable String appId) {
        ApplicationResponseDto application = applicationService.getApplicationDtoByAppId(appId);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(application)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerEmail}")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getAllApplicationByOwnerEmail(@PathVariable String ownerEmail) {
        List<ApplicationResponseDto> applications = applicationService.getAllApplicationDtoByOwnerEmail(ownerEmail);
        ApiResponse<List<ApplicationResponseDto>> response = ApiResponse.<List<ApplicationResponseDto>>builder()
                .success(true)
                .message("Application retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(applications)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/token-sign-key/{appId}")
    public ResponseEntity<ApiResponse<String>> getJwtSecretByAppId(@PathVariable String appId) {
        String secret = applicationService.getApplicationJwtSecretString(appId);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Application retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(secret)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> registerNewApp(@Valid @RequestBody ApplicationRequestDto dto) {
        ApplicationResponseDto application = applicationService.registerNewApp(dto);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application registered successfully")
                .timestamp(LocalDateTime.now())
                .data(application)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{appId}")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> updateApplication(
            @PathVariable String appId,
            @RequestBody Map<String, Object> updates) {
        ApplicationResponseDto application = applicationService.updateApplication(appId, updates);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application updated successfully")
                .timestamp(LocalDateTime.now())
                .data(application)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> deleteApplication(@PathVariable String appId) {
        ApplicationResponseDto application = applicationService.deleteApplication(appId);
        ApiResponse<ApplicationResponseDto> response = ApiResponse.<ApplicationResponseDto>builder()
                .success(true)
                .message("Application deleted successfully")
                .timestamp(LocalDateTime.now())
                .data(application)
                .build();
        return ResponseEntity.ok(response);
    }
}
