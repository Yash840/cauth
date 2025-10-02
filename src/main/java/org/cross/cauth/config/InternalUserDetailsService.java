package org.cross.cauth.config;

import lombok.RequiredArgsConstructor;
import org.cross.cauth.Organization.domain.Organization;
import org.cross.cauth.Organization.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class InternalUserDetailsService implements UserDetailsService {
    private final OrganizationRepository organizationRepository;

    Logger logger = LoggerFactory.getLogger(InternalUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername : entered into InternalUserDetailsService, attempting to fetching user from database");

        Organization user = organizationRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getHashedPassword(),
                Stream.of("ROLE_ORGANIZATION")
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }
}
