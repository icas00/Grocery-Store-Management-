package grocery.exception;

/**
 * Custom exception for general application errors.
 * It's an unchecked exception.
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
