package org.cross.cauth.utils;

public interface UniqueIdGenerator {
    String generate();

    String generateSLCode(int length);
}
