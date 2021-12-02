package project.lab6.service;

/**
 * class for exceptions that occur at validation
 */
public class ServiceException extends RuntimeException {
    public ServiceException() {

    }

    /**
     * constructor
     *
     * @param message - the message to pe printed
     */
    public ServiceException(String message) {
        super(message);
    }
}
