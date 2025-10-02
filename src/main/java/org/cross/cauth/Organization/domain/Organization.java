package org.cross.cauth.Organization.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String orgName;

    private String email;

    private String hashedPassword;

    private boolean isActive;

    private LocalDate joinedOn;

    private String role;

    public Organization() {
        this.role = "ROLE_ORGANIZATION";
    }

    public Organization(String orgName, String email, String hashedPassword, boolean isActive) {
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.orgName = orgName;
        this.isActive = isActive;
        this.role = "ROLE_ORGANIZATION";
    }

    @PrePersist
    public void setJoinedOn(){
        this.joinedOn = LocalDate.now();
    }
}
