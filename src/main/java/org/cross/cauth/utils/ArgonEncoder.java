package org.cross.cauth.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ArgonEncoder implements CredentialSecurityManager {

    Argon2 argon2 = Argon2Factory.create();

    private final int ITERATIONS = 12;
    private final int PARALLELISM = 1;
    private final int MEMORY_COST = 65336;


    @Override
    public String encode(String plainText) {
        char[] password = plainText != null ? plainText.toCharArray() : new char[0];
        try{
            return argon2.hash(
                    ITERATIONS, MEMORY_COST, PARALLELISM, password
            );
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    @Override
    public boolean isValid(String plainText, String hashedText) {
        return argon2.verify(hashedText, plainText);
    }

}
