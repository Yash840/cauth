package org.cross.cauth.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

@Component
public class InBuiltIdGenerator implements UniqueIdGenerator {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String PREFIX = "APP.";
    private static final int SUFFIX_LENGTH = 8;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Generate app ID with alphanumeric suffix for enhanced security
     * Uses A-Z and 0-9 characters providing 36^8 possible combinations
     */
    @Override
    public String generate(){
        StringBuilder sb = new StringBuilder();

        sb.append(PREFIX);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        sb.append(timestamp);
        sb.append(".");

        // Generate secure random alphanumeric suffix
        for (int i = 0; i < SUFFIX_LENGTH; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    @Override
    public String generateSLCode(int length) {
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int idx = secureRandom.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(idx));
        }

        return code.toString();
    }

}
