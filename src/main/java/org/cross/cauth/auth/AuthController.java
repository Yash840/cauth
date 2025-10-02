package org.cross.cauth.auth;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.cross.cauth.ApiResponse;
import org.cross.cauth.Organization.OrganizationService;
import org.cross.cauth.Organization.dto.CreateOrgDto;
import org.cross.cauth.Organization.dto.LoginOrgDto;
import org.cross.cauth.Organization.dto.OrgPublicDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling authentication operations
 * related to {@code Organization} entities within the authentication service.
 * <p>
 * This controller provides endpoints to:
 * <ul>
 *     <li>Authenticate an organization using credentials.</li>
 *     <li>Generate authorization codes and tokens scoped to an organization.</li>
 *     <li>Refresh or revoke existing organization authentication tokens.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * Clients (such as admin dashboards or backend services) must authenticate
 * at the organization level before performing organization-specific operations,
 * such as project registration or user management.
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Accept and validate organization credentials.</li>
 *     <li>Issue secure authentication artifacts (auth codes, access tokens, refresh tokens).</li>
 *     <li>Ensure proper scoping of tokens to the requesting organization.</li>
 *     <li>Handle authentication-related error responses consistently.</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * - This controller is <b>only</b> responsible for organization-level authentication.
 * - Application-level and end-user authentication should be managed by their
 *   respective controllers/services.
 *
 * @author Yash
 * @version 1.0
 * @since 2025
 */


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public/auth")
public class AuthController {
    private final OrganizationService organizationService;
    private final AuthService authService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<OrgPublicDto>> loginOrg(
            @RequestBody LoginOrgDto dto,
            @NonNull HttpServletResponse response
            ){

        logger.info("loginOrg : login attempt by org {}", dto);

        String token = authService.login(dto);

        Cookie cookie = new Cookie("authToken", token);
        cookie.setMaxAge(60*60*12);
        cookie.setHttpOnly(true);
        cookie.setPath("api/v1");

        response.addCookie(cookie);

        ApiResponse<OrgPublicDto> apiResponse =
                ApiResponse.<OrgPublicDto>builder()
                        .success(true)
                        .message("Organization Logged In Successfully")
                        .data(authService.getOrgDetails(dto.getEmail()))
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<OrgPublicDto>> RegisterOrganization(
            @RequestBody CreateOrgDto dto,
            @NonNull HttpServletResponse response
    ){

        String token = authService.register(dto);

        Cookie cookie = new Cookie("authToken", token);
        cookie.setMaxAge(60*60*12);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        ApiResponse<OrgPublicDto> apiResponse =
                ApiResponse.<OrgPublicDto>builder()
                        .success(true)
                        .message("Organization registered Successfully")
                        .data(authService.getOrgDetails(dto.getEmail()))
                        .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutOrg(
            @NonNull HttpServletResponse response
    ){
        Cookie cookie = new Cookie("authToken", "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("api/v1");

        response.addCookie(cookie);

        ApiResponse<String> apiResponse =
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Organization logged out")
                        .data("Logged Out")
                        .build();

        return ResponseEntity.ok(apiResponse);
    }



}
