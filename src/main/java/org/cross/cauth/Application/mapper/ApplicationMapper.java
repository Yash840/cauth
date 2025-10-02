package org.cross.cauth.Application.mapper;

import org.cross.cauth.Application.domain.Application;
import org.cross.cauth.Application.dto.ApplicationRequestDto;
import org.cross.cauth.Application.dto.ApplicationResponseDto;

public class ApplicationMapper {

    /**
     * Convert ApplicationRequestDto to Application entity
     */
    public static Application toEntity(ApplicationRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }

        Application application = new Application();
        application.setAppName(requestDto.getAppName());
        application.setAppSecret(requestDto.getAppSecret());
        application.setAllowedCallbackUrls(requestDto.getAllowedCallbackUrls());
        application.setOwnerEmail(requestDto.getOwnerEmail());

        return application;
    }

    /**
     * Convert Application entity to ApplicationResponseDto
     */
    public static ApplicationResponseDto toResponseDto(Application application) {
        if (application == null) {
            return null;
        }

        return ApplicationResponseDto.builder()
                .appName(application.getAppName())
                .appId(application.getAppId())
                .dateOfJoining(application.getDateOfJoining())
                .ownerEmail(application.getOwnerEmail())
                .allowedCallbackUrls(application.getAllowedCallbackUrls())
                .build();
    }

    /**
     * Update existing Application entity with data from ApplicationRequestDto
     */
    public static void updateEntity(Application application, ApplicationRequestDto requestDto) {
        if (application == null || requestDto == null) {
            return;
        }

        if (requestDto.getAppName() != null) {
            application.setAppName(requestDto.getAppName());
        }
        if (requestDto.getAppSecret() != null) {
            application.setAppSecret(requestDto.getAppSecret());
        }
        if (requestDto.getAllowedCallbackUrls() != null) {
            application.setAllowedCallbackUrls(requestDto.getAllowedCallbackUrls());
        }
    }
}
