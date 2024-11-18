package k19g.quiz.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <h2>Configuration Class for Defining Application Beans</h2>
 * <p>This class is responsible for creating and configuring various application beans.</p>
 * 
 * <p><strong>Author:</strong> K19G</p>
 */
@Configuration
public class BeanConfiguration {
	
    private static final Logger logger = LoggerFactory.getLogger(BeanConfiguration.class);

    /**
     * <p>Provides a bean for <code>BCryptPasswordEncoder</code>.</p>
     * 
     * <p>This method returns an instance of <code>BCryptPasswordEncoder</code>
     * which is a password encoder implementing the bcrypt hashing function.
     * It is used for securely hashing user passwords before storing them.</p>
     * 
     * @return <code>BCryptPasswordEncoder</code> instance for password hashing
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
    	logger.info("Creating BCryptPasswordEncoder bean for password hashing...");
        return new BCryptPasswordEncoder();
    }

}
