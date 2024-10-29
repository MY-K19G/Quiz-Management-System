package k19g.quiz.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.ModelAndView;

import k19g.quiz.service.CustomUserDetailsService;

/**
 * <h2>Configuration Class for Defining Application Beans</h2>
 * <p>This class is responsible for creating and configuring various application beans.</p>
 * 
 * <p><strong>Author:</strong> K19G</p>
 */
@Configuration
public class BeanConfiguration {
	
	@Autowired
	CustomUserDetailsService customUserDetailsService; 
	
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

    /**
     * Creates a prototype-scoped bean of ModelAndView.
     * <p>
     * This method returns a new instance of <code>ModelAndView each</code> time it is called,
     * allowing for unique configurations for each request or service.
     * ModelAndView is used to hold model data and view name for rendering responses.
     * </p>
     * 
     * @return a new instance of <code>ModelAndView</code>
     */
    @Bean
    @Scope("prototype")
    public ModelAndView getModelAndView() {
    	 logger.info("Creating ModelAndView bean...");
        return new ModelAndView();
    }
    
}
