package k19g.quiz.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
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

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final CustomUserDetailsService customUserDetailsService;
    
    private final BeanConfiguration beanConfiguration;
    
    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, BeanConfiguration beanConfiguration) {
		this.customUserDetailsService = customUserDetailsService;
		this.beanConfiguration = beanConfiguration;
	}
    
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
                .requestMatchers("/register", "/login", "/perform_register", "/validateRegister", "/api/validateRegister").permitAll()

                .requestMatchers("/",  "/quiz", "/processQuizSetup", "/api/get-background-image", "/result", "/api/submit_quiz").permitAll()

                .requestMatchers("/404", "/403", "/500","/error", "/view/**", "/assets/**", "/favicon.ico").permitAll()
                	
                .anyRequest().denyAll()
           )
        .formLogin(form -> form
                .loginPage("/login")  
                .loginProcessingUrl("/perform_login")  
                .usernameParameter("userEmail")  
                .passwordParameter("userPassword")  
                .defaultSuccessUrl("/", false)  
                .failureUrl("/login?error=true")  
                .permitAll()  
        	)
        .logout(logout -> logout
                .logoutUrl("/logout") 
                .logoutSuccessUrl("/login?logout=true") 
                .invalidateHttpSession(true) 
                .deleteCookies("JSESSIONID", "remember-me") 
                .permitAll()
            ) 
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(e -> e
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                	 request.setAttribute("errorMessage", accessDeniedException.getMessage());
                     request.getRequestDispatcher("/view/404.jsp").forward(request, response);
                })
                .authenticationEntryPoint((request, response, authException) -> {
                	request.setAttribute("errorMessage", authException.getMessage());
                    request.getRequestDispatcher("/view/404.jsp").forward(request, response);
                })
            );

        logger.info("Security filter chain configured successfully.");
        
        return http.build();  
    }

    /**
     * Creates and configures an {@link AuthenticationManager} bean for Spring Security.
     * <p>
     * This method sets up the authentication manager to use a custom {@link UserDetailsService}
     * (in this case, {@code customUserDetailsService}) for user authentication and specifies
     * a {@link PasswordEncoder} (in this case, a BCrypt password encoder) to encode and verify passwords.
     * </p>
     *
     * @param http The {@link HttpSecurity} object that is used to build security filters and shared objects,
     *             allowing access to authentication configuration.
     * @return An instance of {@link AuthenticationManager} that will be used by Spring Security for user authentication.
     * @throws Exception if an error occurs while configuring the {@link AuthenticationManager}.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authenticationManagerBuilder.userDetailsService(customUserDetailsService)
            .passwordEncoder(beanConfiguration.bCryptPasswordEncoder());  // Use the BCrypt password encoder

        return authenticationManagerBuilder.build();
    }
    
}

