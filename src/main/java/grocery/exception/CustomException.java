package grocery.exception;

/**
 * Custom exception class for application-specific exceptions.
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}