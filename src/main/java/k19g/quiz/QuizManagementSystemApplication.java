package k19g.quiz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main entry point for the Quiz Management System application.
 * This class contains the main method which launches the Spring Boot application.
 * 
 * Author: K19G
 */
@SpringBootApplication
@EnableCaching
public class QuizManagementSystemApplication{
    private static final Logger logger = LoggerFactory.getLogger(QuizManagementSystemApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(QuizManagementSystemApplication.class, args);
        logger.info("Quiz Management System Application started successfully."); // Log after application starts

    }
    
}
