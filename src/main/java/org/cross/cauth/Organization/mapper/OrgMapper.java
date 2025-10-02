package org.cross.cauth.Organization.mapper;

import org.cross.cauth.Organization.domain.Organization;
import org.cross.cauth.Organization.dto.CreateOrgDto;
import org.cross.cauth.Organization.dto.OrgPublicDto;

public class OrgMapper {

    public static Organization toEntity(CreateOrgDto dto){
        return new Organization(
                dto.getName(),
                dto.getEmail(),
                dto.getPassword(),
                true
        );
    }

    public static OrgPublicDto toPublicDto(Organization org){
        return OrgPublicDto.builder()
                .name(org.getOrgName())
                .email(org.getEmail())
                .isActive(org.isActive())
                .build();
    }
}
