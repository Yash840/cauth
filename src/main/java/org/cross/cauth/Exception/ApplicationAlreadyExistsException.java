package org.cross.cauth.Exception;

public class ApplicationAlreadyExistsException extends RuntimeException {
    public ApplicationAlreadyExistsException(String message) {
        super(message);
    }

    public ApplicationAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
