package k19g.quiz.exception;

/**
 *Exception thrown when a fetching type is missing in Quiz.
 * 
 * <p><b>Author:</b> K19G</p>
 */
public class QuizTypesNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1777127261069651430L;

	public QuizTypesNotFoundException() {
        super("No quiz types found in the repository.");
    }

    public QuizTypesNotFoundException(String message) {
        super(message);
    }

    public QuizTypesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizTypesNotFoundException(Throwable cause) {
        super(cause);
    }
}

