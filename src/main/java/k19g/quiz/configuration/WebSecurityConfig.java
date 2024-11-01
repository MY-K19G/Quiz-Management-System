package k19g.quiz.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import k19g.quiz.service.CustomUserDetailsService;

/**
 * <h2>Configuration class for Spring Security</h2>
 * <p>This class configures security for the application, including authentication, 
 * authorization, login settings, and CSRF protection.</p>
 * 
 * <p><strong>Author:</strong> K19G</p>
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    // CustomUserDetailsService to load user-specific data during authentication
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // BeanConfiguration to inject the BCryptPasswordEncoder bean
    @Autowired
    private BeanConfiguration beanConfiguration;
    
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);


    /**
     * <p>Defines the security filter chain, setting configurations such as permitted URLs, 
     * form login, and CSRF protection.</p>
     * 
     * @param http the <code>HttpSecurity</code> object to configure the application's security.
     * @return <code>SecurityFilterChain</code> that defines the security filter chain.
     * @throws Exception if there is an error during configuration.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	 logger.info("Configuring security filter chain...");

        http.authorizeHttpRequests(authz -> authz
                // Permitting access to H2 console, registration, and login pages without authentication 
                .requestMatchers("/h2-console/**","/register", "/view/quizAdminRegister.jsp", "/login", "/view/quizAdminLogin.jsp", "/perform_register").permitAll()

                // Allow public access to home, quiz assessment, participant, and result pages
                .requestMatchers("/", "/view/app/quizAssessmentDetails.jsp", "/quiz", "/view/app/quizParticipantPage.jsp", "/result", "/view/app/quizResultsDashboard.jsp",  "/api/submit_quiz", "/processQuizSetup").permitAll()

                // Permitting error pages without authentication
                .requestMatchers("/404", "/view/404.jsp", "/403", "/view/403.jsp", "/500", "/view/500.jsp", "/view/**", "/static/**","/assets/**" ).permitAll()
                	
                // All other requests require authentication
                .anyRequest().authenticated()
        )
        .formLogin(form -> form
                // Custom login page configuration
                .loginPage("/login")  // URL of the login page
                .loginProcessingUrl("/perform_login")  // URL to submit the login form
                .usernameParameter("userEmail")  // Login form username field (email)
                .passwordParameter("userPassword")  // Login form password field
                .defaultSuccessUrl("/", true)  // Redirect to home page after successful login
                .failureUrl("/login?error=true")  // Redirect to login page with error message on failure
                .permitAll()  // Allow access to login page without authentication
        ).exceptionHandling(exceptionHandling -> 
        exceptionHandling
        .accessDeniedHandler(accessDeniedHandler()) // Custom 403 handler
        )
        .csrf(csrf -> csrf.disable())  // Disabling CSRF protection for simplicity (should enable for production)
        .headers(headers -> headers.frameOptions(fo -> fo.disable()));  // Disabling frame options for H2 console

        logger.info("Security filter chain configured successfully.");
        
        return http.build();  // Return the configured SecurityFilterChain
    }

    /**
     * <p>Configures global authentication with a custom <code>UserDetailsService</code> 
     * and <code>BCryptPasswordEncoder</code>.</p>
     * 
     * @param auth the <code>AuthenticationManagerBuilder</code> to build the authentication manager.
     * @throws Exception if there is an error during authentication configuration.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	logger.info("Configuring global authentication with CustomUserDetailsService and BCryptPasswordEncoder...");
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(beanConfiguration.bCryptPasswordEncoder());  // Use BCryptPasswordEncoder for password hashing
    }
    
    
    /**
     * <p>Configures a custom <code>AccessDeniedHandler</code> bean. This handler is triggered when a user 
     * attempts to access a resource they do not have permission for, redirecting them to the <code>/403</code> 
     * error page.</p>
     *
     * @return an <code>AccessDeniedHandler</code> instance configured to redirect to <code>/403</code>
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            logger.warn("Access denied for user: {}. Redirecting to /403 page.", request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "unknown user");
            response.sendRedirect("/403");
        };
    }
    
   
    
}

