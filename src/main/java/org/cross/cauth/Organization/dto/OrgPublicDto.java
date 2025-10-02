package org.cross.cauth.Organization.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrgPublicDto {
    private String name;
    private String email;
    private boolean isActive;
}
