package org.cross.cauth.ClientAuthService;

import lombok.RequiredArgsConstructor;
import org.cross.cauth.Application.Service.ApplicationService;
import org.cross.cauth.EmailService.MailService;
import org.cross.cauth.Exception.InvalidCredentialsException;
import org.cross.cauth.Jwt.JwtSecretService;
import org.cross.cauth.Jwt.JwtService;
import org.cross.cauth.User.Service.UserService;
import org.cross.cauth.User.dto.CreateUserRequestDto;
import org.cross.cauth.User.dto.UserLoginRequestDto;
import org.cross.cauth.User.dto.UserResponseDto;
import org.cross.cauth.utils.SecretCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class ClientAuthService {
    private final UserService userService;
    private final ApplicationService applicationService;
    private final JwtService jwtService;
    private final JwtSecretService jwtSecretService;
    private final SecretCodeService secretCodeService;
    private final MailService mailService;

    Logger logger = LoggerFactory.getLogger(ClientAuthService.class);

    public boolean validateUser(String email, String appId, String password){
        return userService.isValidUser(email, appId, password);
    }

    public String login(UserLoginRequestDto dto){
        String email = dto.getEmail();
        String password = dto.getPassword();
        String appId = dto.getAppId();

        if(validateUser(email, appId, password)){
            SecretKey signInKey = jwtSecretService.getJwtSecret(appId);

            return jwtService.generateTokenForClient(email, appId, signInKey);
        }

        throw new InvalidCredentialsException("Bad Credentials");
    }

    public String register(CreateUserRequestDto dto){
        String email = dto.getEmail();
        String appId = dto.getAppId();

        userService.createUser(dto);
        SecretKey signInKey = jwtSecretService.getJwtSecret(appId);

        return jwtService.generateTokenForClient(email, appId, signInKey);
    }

    public String getUserAuthId(UserIdentificationDto dto){
        return userService.identifyUserByEmailAndAppId(dto.getEmail(), dto.getAppId());
    }

    public String issuePassResetCode(String authId){
        return secretCodeService.generateUserPasswordResetCode(authId);
    }

    public String resetPassword(ResetPasswordRequestDto dto){
        logger.info("resetPassword : attempting to reset password");

        String securityCode = dto.getSecurityCode();

        logger.info("resetPassword : got security code {}", securityCode);

        String authId = secretCodeService.consumeResetPassCode(securityCode);

        logger.info("resetPassword : got auth id {}", authId);

        //Validating auth Id provided by user and associated with security code
        if(!dto.getAuthId().equals(authId)){
            throw new InvalidCredentialsException("Invalid auth id");
        }

        UserResponseDto user = userService.resetPassword(authId, dto.getNewPassword());

        logger.info("resetPassword : user password updated user {}", user);
        return "Password Changed";
    }

    public String issueAuthToken(TokenRequestDto dto){
        logger.info("issueAuthToken : initializing auth token generation");

        if(!applicationService.verifyApplication(dto.getAppId(), dto.getAppSecret())){
            logger.info("issueAuthToken : auth token generation failed due to app credentials are not valid");
            throw new InvalidCredentialsException("Bad Credentials");
        }

        String authCode = dto.getAuthCode();

        String authString = secretCodeService.consumeAuthCode(authCode);

        logger.info("issueAuthToken : auth string extracted {}", authString);

        String email = authString.split(":")[1];
        String appId = authString.split(":")[0];

        logger.info("issueAuthToken : email {} appId {}", email, appId);

        SecretKey signInKey = jwtSecretService.getJwtSecret(appId);

        return jwtService.generateTokenForClient(email, appId, signInKey);
    }


}
