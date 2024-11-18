package k19g.quiz.exception;


public class QuizAnswerNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 2649634770167465595L;


    public QuizAnswerNotFoundException() {
        super("No quiz categories found in the repository.");
    }

    public QuizAnswerNotFoundException(String message) {
        super(message);
    }

    public QuizAnswerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuizAnswerNotFoundException(Throwable cause) {
        super(cause);
    }
}
