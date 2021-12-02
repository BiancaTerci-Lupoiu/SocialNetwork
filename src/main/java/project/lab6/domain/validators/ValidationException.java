package project.lab6.domain.validators;

/**
 * class for exceptions that occur at validation
 */
public class ValidationException extends RuntimeException {

    public ValidationException() {

    }

    /**
     * constructor
     *
     * @param message - the message to pe printed
     */
    public ValidationException(String message) {
        super(message);
    }

    // in plus
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
