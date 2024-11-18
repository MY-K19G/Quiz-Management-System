package k19g.quiz.exception;


/**
 *Exception thrown when unable to delete a Quiz.
 * 
 * <p><b>Author:</b> K19G</p>
 */
public class QuizDeletionException extends RuntimeException {
	
	private static final long serialVersionUID = -2269885240753218293L;
	
	public QuizDeletionException() {
        super("Quiz ID cannot be null or invalid.");
    }
	
    public QuizDeletionException(String message) {
        super(message);
    }

    public QuizDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public QuizDeletionException(Throwable cause) {
        super(cause);
    }
}
