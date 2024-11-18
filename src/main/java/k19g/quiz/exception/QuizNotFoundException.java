package k19g.quiz.exception;

/**
 *Exception thrown when a fetching Quiz is not present in database.
 * 
 * <p><b>Author:</b> K19G</p>
 */
public class QuizNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1442464717702791107L;

    public QuizNotFoundException() {
        super("Quiz object cannot be null.");
    }

    public QuizNotFoundException(String message) {
        super(message);
    }
    
    public QuizNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizNotFoundException(Throwable cause) {
        super(cause);
    }
}
