package k19g.quiz.exception;

/**
 *Exception thrown when a fetching question title  is missing from Quiz.
 * 
 * <p><b>Author:</b> K19G</p>
 */
public class QuizTitlesNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3597465187108696869L;

	public QuizTitlesNotFoundException() {
        super("No quiz question titles found in the repository.");
    }

    public QuizTitlesNotFoundException(String message) {
        super(message);
    }

    public QuizTitlesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizTitlesNotFoundException(Throwable cause) {
        super(cause);
    }
}

