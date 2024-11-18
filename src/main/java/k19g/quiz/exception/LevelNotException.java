package k19g.quiz.exception;

/**
 *Exception thrown when a fetching level is missing.
 * 
 * <p><b>Author:</b> K19G</p>
 */
public class LevelNotException extends RuntimeException {
	
	private static final long serialVersionUID = -2677900267202212608L;
	
	public LevelNotException() {
		super("No Level Found.");
    }
	
    public LevelNotException(String message) {
        super(message);
    }
    
    public LevelNotException(String message, Throwable cause) {
        super(message, cause);
    }

    public LevelNotException(Throwable cause) {
        super(cause);
    }
}