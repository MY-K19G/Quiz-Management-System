package k19g.quiz.exception;

/**
 *Exception thrown when a Quiz id is not present in Quiz.
 * 
 * <p><b>Author:</b> K19G</p>
 */
public class QuizIdInvalidException extends IllegalArgumentException {

	private static final long serialVersionUID = 2879345746399758520L;
	
    public QuizIdInvalidException() {
        super("Quiz ID cannot be null or invalid.");
    }

    public QuizIdInvalidException(String message) {
        super(message);
    }

    public QuizIdInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizIdInvalidException(Throwable cause) {
        super(cause);
    }
}

