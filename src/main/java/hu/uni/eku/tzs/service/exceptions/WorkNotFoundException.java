package hu.uni.eku.tzs.service.exceptions;

public class WorkNotFoundException extends Exception {

    public WorkNotFoundException() {

    }

    public WorkNotFoundException(String message) {
        super(message);
    }

    public WorkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkNotFoundException(Throwable cause) {
        super(cause);
    }

    public WorkNotFoundException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
