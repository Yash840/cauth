package org.cross.cauth.Application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponseDto {
    private String appName;
    private String appId;
    private String ownerEmail;
    private LocalDateTime dateOfJoining;
    private List<String> allowedCallbackUrls;
}
