package hu.uni.eku.tzs.service.exceptions;

public class CharacterAlreadyExistsException extends Exception {

    public CharacterAlreadyExistsException() {

    }

    public CharacterAlreadyExistsException(String message) {
        super(message);
    }

    public CharacterAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CharacterAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public CharacterAlreadyExistsException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
