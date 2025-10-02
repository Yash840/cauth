package org.cross.cauth.Organization.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginOrgDto {
    private String email;
    private String password;
}
