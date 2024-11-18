package k19g.quiz.exception;

/**
 * Exception thrown when a required session attribute is missing.
 * 
 * <p>This exception is a runtime exception that indicates that a necessary attribute
 * expected to be present in the session is not found. It can be used to signal errors
 * related to session management in the application.</p>
 * 
 * <p><strong>Author:</strong> K19G</p>
 */
public class MissingSessionAttributeException extends RuntimeException {
    
    private static final long serialVersionUID = 6714628432234854602L;

    public MissingSessionAttributeException() {
		super("No session Found.");
    }
    
    public MissingSessionAttributeException(String message) {
        super(message);
    }
    
    public MissingSessionAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingSessionAttributeException(Throwable cause) {
        super(cause);
    }
}

