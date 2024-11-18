package k19g.quiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import k19g.quiz.entity.User;
import k19g.quiz.service.ApplicationSettingsService;
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
	
	private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);
	 
	private final UserAccountService userAccountService;
	private final ApplicationSettingsService applicationSettingsService;
    private boolean isRegisterEnabled=true; 

    @Autowired
    private UserAccountController(UserAccountService userAccountService,
    		ApplicationSettingsService applicationSettingsService
    		) {
    	this.userAccountService=userAccountService;
    	this.applicationSettingsService=applicationSettingsService;
    }
    
	/**
	 * Handles the HTTP GET request for displaying the admin login page.
	 * 
	 * <p>This method returns the login view for quiz administrators.
	 * It ensures that the login page is rendered properly when accessed.
	 * 
	 * @return A string representing the name of the login view template.
	 */
	@GetMapping("/login")
	public String showLoginPage() {

		logger.info("Accessing the admin login page.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null 
        	 && authentication.isAuthenticated() 
        	 && !(authentication instanceof AnonymousAuthenticationToken)) {
        	    
        	    logger.info("User is already logged in. Redirecting to dashboard.");
        	    return "redirect:/";
        	}
	    return "quizAdminLogin";
	}

	
	/**
	 * Handles the HTTP GET request for displaying the admin registration page.
	 * 
	 * <p>This method returns the registration view for quiz administrators.
	 * It ensures that the registration page is rendered properly when accessed.</p>
	 * 
	 * @return A string representing the name of the registration view template.
	 */
	@GetMapping("/register")
	public String showRegisterPage() {
        logger.info("Accessing the admin registration page.");
        
        isRegisterEnabled=applicationSettingsService.getToggleRegister();

        return isRegisterEnabled ? "quizAdminRegister" : null;
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
	public ModelAndView registerPage(@ModelAttribute User user ,RedirectAttributes redirectAttributes) {
			ModelAndView mav= new ModelAndView("redirect:/register");
		
	    if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
            logger.warn("User registration failed: Email is missing.");
            redirectAttributes.addFlashAttribute("errorMessage", "Email is missing.");
            return mav;
	    }

	    if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
            logger.warn("User registration failed: Password is missing.");
            redirectAttributes.addFlashAttribute("error", "Wrong password for "+ user.getUserEmail());
            return mav;
	    }
		
	    user.setUserRole("ADMIN");

        logger.info("Attempting to register new user with email: {}", user.getUserEmail());
	
		userAccountService.createUser(user.getUserEmail(),user.getUserPassword() , user.getUserRole());
		
        logger.info("User registered successfully: {}", user.getUserEmail());
        
        mav.setViewName("redirect:/login");
        
		return mav;
	}
	
	
	
	
	/**
	 * Handles the login functionality for quiz administrators.
	 * 
	 * <p>This method processes the login form submission and 
	 * redirects the user to the quiz creation page.</p>
	 * 
	 * @param user The User object containing the submitted login credentials.
	 * @return A string representing the redirect to the quiz creation page.
	 */
	@PostMapping("/perform_login")
	public String loginValidation(@ModelAttribute User user) {
        
		logger.info("User login attempted with email: {}", user.getUserEmail());

	    return "redirect:/create";
	}

}
