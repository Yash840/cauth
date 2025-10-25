package org.cross.cauth.Application.Service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.cross.cauth.Application.Repository.ApplicationRepository;
import org.cross.cauth.Application.domain.Application;
import org.cross.cauth.Application.dto.ApplicationRequestDto;
import org.cross.cauth.Application.dto.ApplicationResponseDto;
import org.cross.cauth.Application.mapper.ApplicationMapper;
import org.cross.cauth.Exception.ApplicationAlreadyExistsException;
import org.cross.cauth.Exception.ApplicationNotFoundException;
import org.cross.cauth.Exception.UserNotFoundException;
import org.cross.cauth.Jwt.JwtSecretService;
import org.cross.cauth.Organization.OrganizationService;
import org.cross.cauth.utils.CredentialSecurityManager;
import org.cross.cauth.utils.UniqueIdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@Data
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JwtSecretService jwtSecretService;
    private final OrganizationService organizationService;
    private final CredentialSecurityManager securityManager;
    private final UniqueIdGenerator appIdGenerator;

    /**
     * Get all registered applications
     * @return List of all appId
     */
    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getAllApplications(){
        return applicationRepository.findAll()
                .stream()
                .map(ApplicationMapper::toResponseDto)
                .toList();
    }

    /**
     * Get appId using app id
     * @param appId applications unique app id
     * @return ApplicationResponseDto
     * @throws ApplicationNotFoundException if app id is invalid
     */
    @Transactional(readOnly = true)
    public Application getApplicationByAppId(String appId){
        return applicationRepository.findByAppId(appId).orElseThrow(() -> new ApplicationNotFoundException("Invalid app id"));
    }

    @Transactional(readOnly = true)
    public ApplicationResponseDto getApplicationDtoByAppId(String appId){
        Application app =  getApplicationByAppId(appId);
        return ApplicationMapper.toResponseDto(app);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getAllApplicationDtoByOwnerEmail(String ownerEmail){
        return applicationRepository.findByOwnerEmail(ownerEmail)
                .stream()
                .map(ApplicationMapper::toResponseDto)
                .toList();
    }

    /**
     * @deprecated
     * Update appId details
     * @param appId applications unique app id
     * @param updates map of all desired updates
     * @return ApplicationResponseDto
     * @throws ApplicationNotFoundException if app id is invalid
     */
    public ApplicationResponseDto updateApplication(String appId, Map<String, Object> updates){
        Application app = applicationRepository.findByAppId(appId).orElseThrow(() -> new ApplicationNotFoundException("Invalid app id"));

        updates.forEach(
                (fieldName, fieldValue) -> {
                    try {
                        Field field = Application.class.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        field.set(app, fieldValue);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        return ApplicationMapper.toResponseDto(applicationRepository.save(app));
    }

    /**
     * Delete appId using app id
     * @param appId appid
     * @return ApplicationResponseDto
     * @throws ApplicationNotFoundException if app id is invalid
     */
    public ApplicationResponseDto deleteApplication(String appId){
        Application app = applicationRepository.findByAppId(appId).orElseThrow(() -> new ApplicationNotFoundException("Invalid app id"));
        applicationRepository.deleteByAppId(appId);
        return ApplicationMapper.toResponseDto(app);
    }

    /**
     * Returns jwt secret used to sign auth tokens
     * @param appId App Id
     * @return String - 312 character jwt secret
     */
    @Transactional(readOnly = true)
    public String getApplicationJwtSecret(String appId){
        return jwtSecretService.getJwtSecret(appId).toString();
    }

    @Transactional(readOnly = true)
    public String getApplicationJwtSecretString(String appId){
        return jwtSecretService.getJwtSecretString(appId);
    }

    /**
     * Registers new appId
     * @param dto ApplicationRequestDto
     * @return ApplicationResponseDto
     * @throws ApplicationAlreadyExistsException if app name is already used by someone
     */
    public ApplicationResponseDto registerNewApp(ApplicationRequestDto dto){
        if(!organizationService.doExist(dto.getOwnerEmail())){
            throw new UserNotFoundException("Owner is not registered");
        }

        if(applicationRepository.findByAppName(dto.getAppName()).isPresent()){
            throw new ApplicationAlreadyExistsException("App name is already taken");
        }

        Application app = ApplicationMapper.toEntity(dto);

        //Generate unique and cryptographically secured app id
        String appId = appIdGenerator.generate();

        app.setAppId(appId);

        //encode app secret before storing it
        app.setAppSecret(securityManager.encode(app.getAppSecret()));

        jwtSecretService.createJwtSecret(appId);

        return ApplicationMapper.toResponseDto(applicationRepository.save(app));
    }

    @Transactional(readOnly = true)
    public boolean verifyApplication(String appId, String appSecret){
        Application app = getApplicationByAppId(appId);
        String hashedSecret = app.getAppSecret();

        return securityManager.isValid(appSecret, hashedSecret);
    }
}
