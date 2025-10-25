package org.cross.cauth.ClientAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cross.cauth.ApiResponse;
import org.cross.cauth.User.dto.CreateUserRequestDto;
import org.cross.cauth.User.dto.UserLoginRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/services/cauth1")
@RequiredArgsConstructor
public class ClientAuthController {
    private final ClientAuthService clientAuthTokenService;

    @PostMapping("/signInWithEmailAndPassword")
    public ResponseEntity<ApiResponse<String>> loginEndUser(
            @RequestBody UserLoginRequestDto dto
    ){
        String authToken = clientAuthTokenService.login(dto);

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .success(true)
                .message("auth token generated successfully")
                .data(authToken)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/signUpWithEmail")
    public ResponseEntity<ApiResponse<String>> registerEndUser(
            @RequestBody CreateUserRequestDto dto
    ){
        String authCode = clientAuthTokenService.register(dto);

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .success(true)
                .message("auth code generated successfully")
                .data(authCode)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/getAuthId")
    public ResponseEntity<ApiResponse<String>> getAuthIdEndUser(
            @RequestBody UserIdentificationDto dto
    ){
        String authId = clientAuthTokenService.getUserAuthId(dto);

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .success(true)
                .message("auth code generated successfully")
                .data(authId)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getResetPasswordCode/{authId}")
    public ResponseEntity<ApiResponse<String>> getResetPasswordCode(
            @PathVariable String authId
    ){
        String code = clientAuthTokenService.issuePassResetCode(authId);

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .success(true)
                .message("auth code generated successfully")
                .data(code)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse<String>> getAuthIdEndUser(
            @RequestBody ResetPasswordRequestDto dto
    ){
        String success = clientAuthTokenService.resetPassword(dto);

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .success(true)
                .message("password changed successfully")
                .data(success)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping(value = "/token")
    public ResponseEntity<ApiResponse<String>> getClientAuthToken(
            @Valid @RequestBody TokenRequestDto dto
    ){
        String authToken = clientAuthTokenService.issueAuthToken(dto);

        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .success(true)
                .message("client auth token generated")
                .data(authToken)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
