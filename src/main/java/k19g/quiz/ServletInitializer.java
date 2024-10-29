package k19g.quiz;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Servlet initializer for the Quiz Management System application.
 * This class extends SpringBootServletInitializer and is used to configure the application
 * when it is deployed to a servlet container.
 * 
 * Author: K19G
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * Configures the Spring application for the servlet context.
     * This method is called when the application is deployed in a servlet container.
     *
     * @param application the Spring application builder
     * @return the configured application builder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(QuizManagementSystemApplication.class);
    }
}

