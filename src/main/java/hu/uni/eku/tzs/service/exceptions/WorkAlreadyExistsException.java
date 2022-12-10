package hu.uni.eku.tzs.service.exceptions;

public class WorkAlreadyExistsException extends Exception {

    public WorkAlreadyExistsException() {

    }

    public WorkAlreadyExistsException(String message) {
        super(message);
    }

    public WorkAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public WorkAlreadyExistsException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
