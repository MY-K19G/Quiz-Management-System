package k19g.quiz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import k19g.quiz.entity.User;
import k19g.quiz.repository.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Set;

/**
 * Custom implementation of UserDetailsService to load user-specific data.
 * This service fetches user details from the database using the UserRepository.
 * 
 * <p><b>Author:</b> K19G</p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    private final UserRepository userRepository;
    
    @Autowired
    CustomUserDetailsService(UserRepository userRepository){
    	this.userRepository=userRepository;
    }

    /**
     * Loads a user from the database based on their email and 
     * returns a UserDetails object for authentication purposes.
     * 
     * @param userEmail The email of the user trying to log in.
     * @return UserDetails containing the user’s credentials and roles.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        logger.info("Attempting to load user by email: {}", userEmail);

        User user = userRepository.findByuserEmail(userEmail);
        
        if (user == null) {
            logger.error("User not found with email: {}", userEmail);
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }
        
        logger.info("User found: {} with role: {}", user.getUserEmail(), user.getUserRole());
        
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getUserRole()));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserEmail())
                .password(user.getUserPassword())
                .authorities(authorities)
                .build();
    }

}
