package org.cross.cauth.utils;

public interface CredentialSecurityManager {
    String encode(String plainText);
    boolean isValid(String plainText, String hashedText);
}
