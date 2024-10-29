package k19g.quiz.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(MissingSessionAttributeException.class);

    
    /**
     * Constructs a new MissingSessionAttributeException with the specified detail message.
     * 
     * @param message the detail message explaining the reason for the exception
     */
    public MissingSessionAttributeException(String message) {
        super(message);
        logger.error("Session attribute missing");

    }
}

