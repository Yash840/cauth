package org.cross.cauth.Organization;

import lombok.RequiredArgsConstructor;
import org.cross.cauth.Exception.InvalidCredentialsException;
import org.cross.cauth.Organization.domain.Organization;
import org.cross.cauth.Organization.dto.CreateOrgDto;
import org.cross.cauth.Organization.dto.OrgPublicDto;
import org.cross.cauth.Organization.mapper.OrgMapper;
import org.cross.cauth.Organization.repository.OrganizationRepository;
import org.cross.cauth.utils.CredentialSecurityManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final CredentialSecurityManager credentialSecurityManager;

    public List<OrgPublicDto> getAllOrgs(){
        return organizationRepository.findAll()
                .stream()
                .map(OrgMapper::toPublicDto)
                .toList();
    }

    public Organization registerOrganization(CreateOrgDto dto){
        String email = dto.getEmail();

        if(organizationRepository.existsByEmail(email)){
            throw new InvalidCredentialsException("email already taken");
        }

        Organization org = OrgMapper.toEntity(dto);

        String hashedPassword = credentialSecurityManager.encode(dto.getPassword());

        org.setHashedPassword(hashedPassword);

        return organizationRepository.save(org);
    }

    public OrgPublicDto registerOrganizationAndGetDto(CreateOrgDto dto){
        Organization org = registerOrganization(dto);

        return OrgMapper.toPublicDto(org);
    }

    public Organization getOrganizationByEmail(String email){
        return organizationRepository
                .findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("invalid email"));
    }

    public OrgPublicDto getOrganizationDtoByEmail(String email){
        return OrgMapper.toPublicDto(getOrganizationByEmail(email));
    }

    public boolean verifyPassword(String email, String plain){
        String hash = getOrganizationByEmail(email).getHashedPassword();
        return credentialSecurityManager.isValid(plain,hash);
    }

    public boolean doExist(String email){
        return organizationRepository.existsByEmail(email);
    }
}
