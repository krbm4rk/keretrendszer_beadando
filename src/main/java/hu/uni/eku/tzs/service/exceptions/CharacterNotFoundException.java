package hu.uni.eku.tzs.service.exceptions;

public class CharacterNotFoundException extends Exception {

    public CharacterNotFoundException() {

    }

    public CharacterNotFoundException(String message) {
        super(message);
    }

    public CharacterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CharacterNotFoundException(Throwable cause) {
        super(cause);
    }

    public CharacterNotFoundException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
