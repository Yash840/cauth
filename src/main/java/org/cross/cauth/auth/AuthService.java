package org.cross.cauth.auth;

import lombok.RequiredArgsConstructor;
import org.cross.cauth.Exception.InvalidCredentialsException;
import org.cross.cauth.Jwt.JwtService;
import org.cross.cauth.Organization.OrganizationService;
import org.cross.cauth.Organization.dto.CreateOrgDto;
import org.cross.cauth.Organization.dto.LoginOrgDto;
import org.cross.cauth.Organization.dto.OrgPublicDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final OrganizationService organizationService;
    private final JwtService jwtService;

    public boolean verifyOrganization(String email, String password){
        if(organizationService.doExist(email) && organizationService.verifyPassword(email, password)){
            return true;
        }

        throw new InvalidCredentialsException("Bad Credentials");
    }

    public String generateAuthenticationToken(String email){
            return jwtService.generateTokenWithRole(email, "ROLE_ORGANIZATION");
    }



    public String login(LoginOrgDto dto){
        String email = dto.getEmail();
        String password = dto.getPassword();

        if(verifyOrganization(email, password)){
            return generateAuthenticationToken(email);
        }

        return null;
    }

    public String register(CreateOrgDto dto){
        organizationService.registerOrganization(dto);
        return generateAuthenticationToken(dto.getEmail());
    }

    public OrgPublicDto getOrgDetails(String email){
        return organizationService.getOrganizationDtoByEmail(email);
    }
}
