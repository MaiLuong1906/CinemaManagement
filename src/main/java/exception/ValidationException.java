package exception;

/**
 * Exception thrown when user input validation fails.
 * Examples: missing required parameters, invalid format, out of range values.
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
