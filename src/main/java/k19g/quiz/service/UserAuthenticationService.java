package k19g.quiz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import k19g.quiz.entity.User;
import k19g.quiz.repository.UserRepository;

/**
 * Service class for user authentication.
 * This class handles the authentication logic for users in the system.
 * 
 * <p><b>Author:</b> K19G</p>
 */
@Service
public class UserAuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationService.class);
    
    private final UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    public UserAuthenticationService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}


    /**
     * Authenticates a user based on their email and password.
     *
     * @param userEmail the email of the user attempting to authenticate
     * @param rawPassword the raw password provided by the user
     * @return true if authentication is successful; false otherwise
     */
    public boolean authenticateUser(String userEmail, String rawPassword) {
        User user = userRepository.findByuserEmail(userEmail);
        if (user == null) {
            logger.warn("Authentication failed: User not found with email: {}", userEmail);
            return false; 
        }
        
        boolean isAuthenticated = passwordEncoder.matches(rawPassword, user.getUserPassword());
        
        if (isAuthenticated) {
            logger.info("Authentication successful for user: {}", userEmail);
        } else {
            logger.warn("Authentication failed: Incorrect password for user: {}", userEmail);
        }

        return isAuthenticated;
    }
}
