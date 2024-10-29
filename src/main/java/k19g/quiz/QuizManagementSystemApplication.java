package k19g.quiz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Quiz Management System application.
 * This class contains the main method which launches the Spring Boot application.
 * 
 * Author: K19G
 */
@SpringBootApplication
public class QuizManagementSystemApplication {
    private static final Logger logger = LoggerFactory.getLogger(QuizManagementSystemApplication.class);

    /**
     * The main method that serves as the entry point of the application.
     * It initializes the Spring Boot application context and starts the application.
     *
     * @param args command-line arguments passed during application startup
     */
    public static void main(String[] args) {
        SpringApplication.run(QuizManagementSystemApplication.class, args);
        logger.info("Quiz Management System Application started successfully."); // Log after application starts

    }
}
