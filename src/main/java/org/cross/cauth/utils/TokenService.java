package org.cross.cauth.utils;

public interface TokenService {
    String generateAuthenticationToken(String email, String appId, String password);

    String generateAuthorizationCode(String email, String appId, String password);
}
