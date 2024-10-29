package k19g.quiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import k19g.quiz.entity.User;
import k19g.quiz.service.CustomUserDetailsService;
import k19g.quiz.service.UserAccountService;

/**
 * Controller for managing user account functionalities such as registration and login.
 * 
 * <p>This class handles the HTTP requests related to user account operations,
 * including user registration and login.</p>
 * 
 * <p><strong>Author:</strong> K19G</p>
 */
@Controller
public class UserAccountController {
	
	@Autowired
	UserAccountService userAccountService;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
    private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);
	
	/**
	 * Handles the HTTP GET request for displaying the admin login page.
	 * 
	 * <p>This method returns the login view for quiz administrators.
	 * It ensures that the login page is rendered properly when accessed.
	 * 
	 * @return A string representing the name of the login view template (e.g., "quizAdminLogin").
	 */
	@GetMapping("/login")
	public String showLoginPage() {
	    // Log the access to the login page for debugging purposes
        logger.info("Accessing the admin login page.");

	    // Return the name of the view to render
	    return "quizAdminLogin";
	}

	
	/**
	 * Handles the HTTP GET request for displaying the admin registration page.
	 * 
	 * <p>This method returns the registration view for quiz administrators.
	 * It ensures that the registration page is rendered properly when accessed.</p>
	 * 
	 * @return A string representing the name of the registration view template (e.g., "quizAdminRegister").
	 */
	@GetMapping("/register")
	public String showRegisterPage() {
	    // Log the access to the registration page for debugging purposes
        logger.info("Accessing the admin registration page.");

	    // Return the name of the view to render the registration page
	    return "quizAdminRegister";
	}

	
	

	
	/**
	 * Handles user registration functionality for quiz administrators.
	 * 
	 * <p>This method processes the registration form submission. It sets the user role
	 * to "ADMIN" and creates a new user account using the provided email and password.
	 * After successful registration, it redirects the user to the login page.</p>
	 * 
	 * @param user The User object containing the submitted registration credentials.
	 * @return A string representing the redirect to the login page.
	 */
	@PostMapping("/perform_register")
	public String registerPage(@ModelAttribute User user ) {
		
		// Check for null or empty user email and password
	    if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
            logger.warn("User registration failed: Email is missing.");
	    }
	    
	    if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
            logger.warn("User registration failed: Password is missing.");
	    }
		
		// Set user role to ADMIN
	    user.setUserRole("ADMIN");

	    // Log the registration attempt
        logger.info("Attempting to register new user with email: {}", user.getUserEmail());
	
	   	// Create a new user account
		userAccountService.createUser(user.getUserEmail(),user.getUserPassword() , user.getUserRole());
		
		// Log successful registration
        logger.info("User registered successfully: {}", user.getUserEmail());

	    // Redirect to login page after successful registration
		return "redirect:/login";
	}
	
	
	
	
	/**
	 * Handles the login functionality for quiz administrators.
	 * 
	 * <p>This method processes the login form submission and redirects the user to the quiz creation page. 
	 * In this simplified version, authentication logic is not handled here.</p>
	 * 
	 * @param user The User object containing the submitted login credentials.
	 * @return A string representing the redirect to the quiz creation page.
	 */
	@PostMapping("/perform_login")
	public String loginValidation(@ModelAttribute User user) {
		// Log successful login
        logger.info("User login attempted with email: {}", user.getUserEmail());

	    // Redirect to the create quiz page
	    return "redirect:/create";
	}
	
}
