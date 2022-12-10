package hu.uni.eku.tzs.service.exceptions;

public class ChapterNotFoundException extends Exception {

    public ChapterNotFoundException() {

    }

    public ChapterNotFoundException(String message) {
        super(message);
    }

    public ChapterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChapterNotFoundException(Throwable cause) {
        super(cause);
    }

    public ChapterNotFoundException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
