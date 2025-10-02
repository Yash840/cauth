package org.cross.cauth.Jwt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtSecretRepository extends JpaRepository<JwtSecretKey, Long> {
    Optional<JwtSecretKey> findByAppId(String id);

    boolean deleteByAppId(String id);
}
