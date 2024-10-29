package k19g.quiz.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * Global error handler for handling common HTTP errors in the application.
 * This class handles specific error codes such as 404, 500, and 403 and logs the details.
 * 
 * <p>
 * It provides a centralized way to manage exceptions and log relevant details, 
 * improving the maintainability and debugging of the application.
 * </p>
 * 
 * <p><b>Author:</b> K19G</p>
 */

@ControllerAdvice
public class ErrorController {

	private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

	/**
     * Handles 404 errors and logs the requested URL.
     *
     * @param e The exception that was thrown when a page is not found.
     * @return The 404 error view name.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404Error(NoHandlerFoundException e,Model model) {
    	model.addAttribute("errorMessage", "Page not found!");
        // Log the URL that caused the 404 error
    	 logger.warn("404 Error - Page not found: {}", e.getRequestURL());
         logger.warn("Exception message: {}", e.getMessage());

        // Return the name of the 404 view
        return "404";  // This should map to a 404.html or 404.jsp in your view folder
    }
    
    /**
     * Handles 500 Internal Server Error and logs the exception details.
     *
     * @param e The exception that caused the internal server error.
     * @return The 500 error view name.
     */
    @ExceptionHandler(Exception.class)
    public String handle500Error(Exception e) {
        // Log the exception details
    	logger.error("500 Internal Server Error occurred.");
        logger.error("Exception message: {}", e.getMessage(), e);  
        // Logs stack trace
        System.err.println("Stack trace:");
        e.printStackTrace();  // Logs the full stack trace

        return "500";  // Maps to 500.jsp or 500.html
    }

    /**
     * Handles 403 Access Denied error and logs the exception details.
     *
     * @param e The exception that caused the access to be denied.
     * @return The 403 error view name.
     */
    @ExceptionHandler(SecurityException.class)
    public String handle403Error(SecurityException e) {
        // Log the exception details
    	logger.error("403 Access Denied occurred.");
        logger.error("Exception message: {}", e.getMessage(), e);  
        // Logs stack trace
        System.err.println("Stack trace:");
        e.printStackTrace();  // Logs the full stack trace

        return "403";  // Maps to 403.jsp or 403.html
    }
}

