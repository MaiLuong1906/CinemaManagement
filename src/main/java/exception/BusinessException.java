package exception;

/**
 * Exception thrown when business logic rules are violated.
 * Examples: booking conflicts, insufficient inventory, invalid state transitions.
 */
public class BusinessException extends Exception {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
