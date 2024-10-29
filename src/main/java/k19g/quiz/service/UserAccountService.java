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
 * @author K19G
 */
@Service
public class UserAccountService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserAccountService.class);
    
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
        user.setUserPassword(passwordEncoder.encode(rawPassword));  // Store the encrypted password
        user.setUserRole(role);
        
        userRepository.save(user); // Save the user to the database

        // Log user creation
        logger.info("User created with email: {}", email);
    }
}
