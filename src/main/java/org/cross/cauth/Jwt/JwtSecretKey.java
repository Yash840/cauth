package org.cross.cauth.Jwt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jwt-secret-keys")
@Getter
@Setter
@NoArgsConstructor
public class JwtSecretKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String appId;

    @Column(length = 325)
    private String secret;

    public JwtSecretKey(String appId, String secret) {
        this.secret = secret;
        this.appId = appId;
    }
}
