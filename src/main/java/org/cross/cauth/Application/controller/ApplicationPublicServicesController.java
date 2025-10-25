package org.cross.cauth.Application.controller;

import lombok.RequiredArgsConstructor;
import org.cross.cauth.ApiResponse;
import org.cross.cauth.Application.Service.ApplicationService;
import org.cross.cauth.Application.dto.AppVerificationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("api/v1/public/apps")
@RequiredArgsConstructor
public class ApplicationPublicServicesController {
    private final ApplicationService applicationService;

    record AppDetails(
            String appId,
            String appSecret,
            String signInKey
    ) {}

    @PostMapping("/verifyAndGetAppDetails")
    public ResponseEntity<ApiResponse<AppDetails>> verifyAndGetAppDetails(@RequestBody AppVerificationDto dto){
        String signInKey = "";
        if(applicationService.verifyApplication(dto.appId(), dto.appSecret())){
            signInKey = applicationService.getApplicationJwtSecretString(dto.appId());
        }
        
        AppDetails appDetails = new AppDetails(dto.appId(), dto.appSecret(), signInKey);

        ApiResponse<AppDetails> response = ApiResponse.<AppDetails>builder()
                .success(true)
                .message("Applications retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(appDetails)
                .build();
        return ResponseEntity.ok(response);
    }
}
