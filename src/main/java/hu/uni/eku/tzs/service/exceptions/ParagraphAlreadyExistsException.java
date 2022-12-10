package hu.uni.eku.tzs.service.exceptions;

public class ParagraphAlreadyExistsException extends Exception {

    public ParagraphAlreadyExistsException() {

    }

    public ParagraphAlreadyExistsException(String message) {
        super(message);
    }

    public ParagraphAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParagraphAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ParagraphAlreadyExistsException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
