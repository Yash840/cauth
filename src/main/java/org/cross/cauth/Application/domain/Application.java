package org.cross.cauth.Application.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    private UUID id;

    @Setter
    @Getter
    private String appId;

    @Setter
    @Getter
    private String ownerEmail;

    @Setter
    @Getter
    private String appSecret;

    @Setter
    @Getter
    private String appName;

    @Setter
    @Getter
    private List<String> allowedCallbackUrls;

    @Getter
    private LocalDateTime dateOfJoining;

    public Application() {
    }

    @PrePersist
    public void setTimestamp(){
        this.dateOfJoining = LocalDateTime.now();
    }
}
