package k19g.quiz.exception;

/**
 *Exception thrown when a fetching quizzes are not present in database.
 * 
 * <p><b>Author:</b> K19G</p>
 */
public class QuizzesNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7686859713919094832L;

    public QuizzesNotFoundException() {
        super("No quizzes found in the repository.");
    }

    public QuizzesNotFoundException(String message) {
        super(message);
    }

    public QuizzesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizzesNotFoundException(Throwable cause) {
        super(cause);
    }
}
