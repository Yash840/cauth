package org.cross.cauth.User.repository;

import org.cross.cauth.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByAuthId(String authId);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndAppId(String email, String appId);

    List<User> findByAppId(String appId);

    List<User> findByIsEmailVerified(Boolean isEmailVerified);

    long countByAppId(String appId);

    boolean existsByEmailAndAppId(String email, String appId);
}
