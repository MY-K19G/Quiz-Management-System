package k19g.quiz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import k19g.quiz.entity.User;
import k19g.quiz.repository.UserRepository;

/**
 * Service class for managing user accounts.
 * This class handles the creation and management of user accounts in the system.
 * 
 * <p><b>Author:</b> K19G</p>
 */
@Service
public class UserAccountService {
	
    private static final Logger logger = LoggerFactory.getLogger(UserAccountService.class);

    private final UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
    public UserAccountService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
    
    /**
     * Creates a new user account.
     *
     * @param email the email of the user to be created
     * @param rawPassword the raw password provided by the user
     * @param role the role assigned to the user
     */
    public void createUser(String email, String rawPassword, String role) {
        
    	User user = new User();
       
    	user.setUserEmail(email);
        user.setUserPassword(passwordEncoder.encode(rawPassword));  
        user.setUserRole(role);
        
        userRepository.save(user);

        logger.info("User created with email: {}", email);
    }
    
}
