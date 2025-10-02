package org.cross.cauth.Application.Repository;

import org.cross.cauth.Application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    Optional<Application> findByAppId(String appId);

    Optional<Application> findByAppName(String appName);

    boolean existsByAppId(String appId);

    void deleteByAppId(String appId);
}
