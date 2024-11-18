package k19g.quiz.exception;

/**
 *Exception thrown when a fetching categories are missing from Quiz.
 * 
 * <p><b>Author:</b> K19G</p>
 */
public class QuizCategoriesNotFoundException extends RuntimeException {
    
	private static final long serialVersionUID = 2649634770167465595L;

    public QuizCategoriesNotFoundException() {
        super("No quiz categories found in the repository.");
    }

    public QuizCategoriesNotFoundException(String message) {
        super(message);
    }

    public QuizCategoriesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizCategoriesNotFoundException(Throwable cause) {
        super(cause);
    }
}
