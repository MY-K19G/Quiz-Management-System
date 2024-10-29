package k19g.quiz.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import k19g.quiz.entity.Level;
import k19g.quiz.entity.Quiz;
import k19g.quiz.repository.QuizRepository;

/**
 * <p>Service class for managing quiz-related operations.
 * This class encapsulates the business logic for fetching, saving, 
 * and manipulating quiz data, including categories, types, questions, and answers.</p>
 * 
 * <p><b>Author:</b> K19G</p>
 */
@Service
public class QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    @Autowired
    private QuizRepository quizRepository;

    /**
     * Retrieves all distinct quiz categories from the quiz repository.
     * 
     * This method fetches the unique categories of quizzes stored in the repository.
     * It logs the action and ensures that valid data is returned.
     * 
     * @return a list of distinct quiz categories
     * @throws IllegalStateException if no quiz categories are found in the repository
     */
    public List<String> getAllCategories() {
        // Log the action of fetching distinct quiz categories
        logger.info("Fetching all distinct quiz categories.");

        // Fetch all distinct quiz categories from the repository
        List<String> quizCategories = quizRepository.findDistinctCategoryBy();

        // Validate that the list of categories is not empty or null
        if (quizCategories == null || quizCategories.isEmpty()) {
            throw new IllegalStateException("No quiz categories found in the repository.");
        }

        // Return the list of distinct quiz categories
        return quizCategories;
    }


    
    
    
    
    /**
     * Retrieves all distinct quiz types from the quiz repository.
     * 
     * This method fetches the unique types of quizzes that are stored in the repository.
     * It logs the action and ensures that the data is available before returning it.
     * 
     * @return a list of distinct quiz types
     * @throws IllegalStateException if no quiz types are found in the repository
     */
    public List<String> getAllTypes() {
        // Log the action of fetching distinct quiz types
        logger.info("Fetching all distinct quiz types.");

        // Fetch all distinct quiz types from the repository
        List<String> quizTypes = quizRepository.findDistinctTypeBy();

        // Validate that the list of quiz types is not empty or null
        if (quizTypes == null || quizTypes.isEmpty()) {
            throw new IllegalStateException("No quiz types found in the repository.");
        }

        // Return the list of distinct quiz types
        return quizTypes;
    }


    
    
    
    
    
    /**
     * Retrieves the titles of all quiz questions from the quiz repository.
     * 
     * This method is responsible for fetching the titles of all quiz questions stored in the repository.
     * It logs the action and performs validation to ensure that the data is available.
     * 
     * @return a list of all quiz question titles
     * @throws IllegalStateException if no quiz titles are found in the repository
     */
    public List<String> getAllQuizQuestionTitle() {
        // Log the action of fetching quiz question titles
        logger.info("Fetching all quiz question titles.");

        // Fetch the titles of all quiz questions
        List<String> quizTitles = quizRepository.findAllQuestionTitles();

        // Validate that the list of quiz titles is not empty
        if (quizTitles == null || quizTitles.isEmpty()) {
            throw new IllegalStateException("No quiz question titles found in the repository.");
        }

        // Return the list of quiz question titles
        return quizTitles;
    }

    
    
    

    /**
     * Retrieves all quiz entries from the quiz repository.
     * 
     * This method is responsible for fetching every quiz available in the repository. 
     * It ensures that the query executes correctly, logs the action, and performs basic validation.
     * 
     * @return a list of all quizzes
     * @throws IllegalStateException if no quizzes are found in the repository
     */
    public List<Quiz> getAllQuizs() {
        // Log the action of fetching all quizzes
        logger.info("Fetching all quizzes.");

        // Fetch all quizzes from the repository
        List<Quiz> quizzes = quizRepository.findAll();

        // Validate that quizzes were found, otherwise throw an exception
        if (quizzes == null || quizzes.isEmpty()) {
            throw new IllegalStateException("No quizzes found in the repository.");
        }

        // Return the list of all quizzes
        return quizzes;
    }


    
    
    
    
    /**
     * Persists a quiz object (representing a quiz entity) into the repository.
     * This method is responsible for adding new quiz or updating 
     * existing quiz in the database.
     * 
     * If the quiz object has an ID, it will update the existing record. If the 
     * ID is null, a new record will be created.
     * 
     * @param quiz the quiz object to be saved or updated. Must not be null.
     * @throws IllegalArgumentException if the quiz object is null.
     */
    public boolean saveQuiz(Quiz quiz) {
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz object cannot be null.");
        }
        logger.info("Attempting to save quiz with title: {} and ID: {}", quiz.getQuestionTitle(), quiz.getId());
        try {
            quizRepository.save(quiz);
            logger.info("Quiz saved successfully with ID: {}", quiz.getId());
            return true; // Indicate success
        } catch (Exception e) {
            logger.error("Failed to save quiz: {}", e.getMessage(), e);
            return false; // Indicate failure
        }
    }

    
    
    

    /**
     * Retrieves a quiz object by its unique identifier (ID).
     * 
     * This method queries the repository to find a quiz with the specified ID. 
     * If a quiz with the provided ID exists, it will be returned as an Optional.
     * If no quiz is found, the Optional will be empty.
     * 
     * @param quizId the unique ID of the quiz to retrieve. Must not be null.
     * @return an Optional containing the quiz if found, otherwise an empty Optional.
     * @throws IllegalArgumentException if the provided quizId is null.
     */
    public Optional<Quiz> getQuiz(Integer quizId) {
        if (quizId == null) {
            throw new IllegalArgumentException("Quiz ID cannot be null.");
        }
        
        logger.info("Fetching quiz with ID: {}", quizId);
        
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        
        if (quiz.isPresent()) {
        	logger.info("Quiz found: {}", quiz.get().getQuestionTitle());
        } else {
        	logger.warn("No quiz found with ID: {}", quizId);
        }
        return quiz;
    }


    
    
    
    /**
     * Deletes a quiz object by its unique identifier (ID) from the repository.
     *
     * This method attempts to delete the quiz associated with the provided ID. 
     * It first counts the number of quizzes before the deletion attempt and 
     * then counts the quizzes again after deletion to verify the operation's success.
     * 
     * @param id the unique ID of the quiz to be deleted. Must not be null.
     * @return true if the quiz was successfully deleted; false otherwise.
     * @throws IllegalArgumentException if the provided ID is null.
     */
    public boolean deleteQuizById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Quiz ID cannot be null.");
        }

        Long beforeQuizSize = quizRepository.count();
        logger.info("Attempting to delete quiz with ID: {}", id);
        
        // Attempt to delete the quiz
        quizRepository.deleteById(id);

        Long afterQuizSize = quizRepository.count();
        System.err.println("Quiz deletion attempted. Size before deletion: " + beforeQuizSize 
                + ", Size after deletion: " + afterQuizSize);

        // Return true if the size has changed, indicating a successful deletion
        boolean deletionSuccessful = !beforeQuizSize.equals(afterQuizSize);
        if (deletionSuccessful) {
            logger.info("Successfully deleted quiz with ID: {}", id);
        } else {
            logger.warn("No quiz found with ID: {} for deletion.", id);
        }
        
        return deletionSuccessful;
    }


    
    
    /**
     * Retrieves a list of random quiz quizzes based on the specified level, category, and type.
     *
     * This method fetches a specified number of quizzes that match the provided criteria.
     * It first converts the level from a string representation to an enum and checks for validity.
     * If valid, it retrieves a paginated list of quizzes that match the given level, category, and type.
     * 
     * @param level the difficulty level of the quizzes (EASY, MEDIUM, HARD, EXPERT)
     * @param category the category of the quizzes
     * @param type the type of the quizzes (e.g., programming, theory)
     * @param numberOfQuestions the number of random quizzes to retrieve (must be positive)
     * @return a list of random quizzes matching the specified criteria
     * @throws IllegalArgumentException if the provided level is invalid or the number of quiz is non-positive
     */
    public List<Quiz> getRandomQuizByLevelCategoryAndType(String level, String category, String type, int numberOfQuestions) {
        if (numberOfQuestions <= 0) {
            throw new IllegalArgumentException("Number of questions must be positive.");
        }

        logger.info("Fetching {} random questions with level: {}, category: {}, type: {}", numberOfQuestions, level, category, type);


        // Convert the level from String to Enum
        Level enumLevel;
        try {
            enumLevel = Level.valueOf(level.toUpperCase());  // Convert level to enum (case-insensitive)
        } catch (IllegalArgumentException e) {
            logger.error("Invalid level provided: {}. Allowed values: EASY, MEDIUM, HARD, EXPERT.", level, e);
            throw new IllegalArgumentException("Invalid level provided. Allowed values: EASY, MEDIUM, HARD, EXPERT.");
        }

        // Create a pageable object to limit the number of quizzes returned
        Pageable pageable = PageRequest.of(0, numberOfQuestions);  // First page, limit the number of quizzes

        // Fetch random quizzes based on level, category, and type with pagination
        Page<Quiz> paginatedQuizzes  = quizRepository.findByLevelCategoryAndType(pageable, enumLevel, category, type);

        logger.info("Fetched {} random quizzes matching criteria.", paginatedQuizzes.getContent().size());
        return paginatedQuizzes.getContent();  // Return the list of random quizzes
    }


    /**
     * Retrieves the answers associated with a specific quizzes based on the given question ID.
     * 
     * This method fetches a list of answers for a given question ID from the quiz repository.
     * It ensures that the provided question ID is valid (non-null and positive) before fetching the data.
     * 
     * @param questionId the ID of the quiz object whose answers are to be retrieved
     * @return a list of integers representing the answers associated with the given question ID
     * @throws IllegalArgumentException if the question ID is null or invalid (non-positive)
     */
    public List<String> getAnswerById(Integer questionId) {
        // Validate the question ID to ensure it is not null or negative
        if (questionId == null || questionId <= 0) {
            throw new IllegalArgumentException("Invalid question ID provided. Question ID must be a positive integer.");
        }

        // Log the action of fetching answers
        logger.info("Fetching answers for question ID: {}", questionId);

        // Fetch and return the answers for the given question ID from the repository
        return quizRepository.findAnswersByQuestionId(questionId);
    }


    /**
     * Retrieves all distinct quiz levels from the quiz repository.
     * 
     * This method is responsible for fetching a list of distinct levels 
     * that are associated with quizzes in the system (e.g., EASY, MEDIUM, HARD, EXPERT).
     * It ensures that the repository query executes successfully and logs the action.
     * 
     * @return a list of distinct quiz levels
     * @throws IllegalStateException if no levels are found in the repository
     */
    public List<Level> getAllDistinctLevels() {
        // Log the action of fetching distinct quiz levels
        logger.info("Fetching all distinct quiz levels.");
        
        // Fetch the distinct levels from the repository
        List<Level> distinctLevels  = quizRepository.findDistinctLevels();
        
     // Sort the distinct levels according to the predefined order of the Level enum
        List<Level> sortedLevels = Arrays.stream(Level.values())
            .filter(level -> distinctLevels.contains(level))
            .collect(Collectors.toList());

        
        // Validate that levels were found, otherwise throw an exception
        if (sortedLevels  == null || sortedLevels.isEmpty()) {
            logger.error("No distinct levels found in the quiz repository.");
            throw new IllegalStateException("No distinct levels found in the quiz repository.");
        }

        logger.info("Fetched distinct quiz levels: {}", sortedLevels);
        // Return the list of distinct levels
        return sortedLevels;
    }
    
    /**
     * Retrieves a shuffled list of answer options for a specific quiz based on the provided quiz ID and reshuffle again.
     *
     * This method queries the quiz repository to fetch a list of answer options associated with the given quiz ID.
     * After fetching the options, it shuffles them to provide a random order each time the quiz is presented.
     *
     * @param quizId the unique ID of the quiz whose answer options are to be retrieved.
     *               Must not be null, or an IllegalArgumentException is thrown.
     * @return a shuffled list of answer options associated with the given quiz ID. If no options are found,
     *         an empty list is returned.
     * @throws IllegalArgumentException if the provided quizId is null.
     */
    public List<String> getRandomOptionsForQuiz(Integer quizId) {
        logger.info("Fetching options for quiz ID: {}", quizId);

        List<String> options = quizRepository.findRandomOptionsByQuizId(quizId);

        // Shuffle the options to get a random order
        Collections.shuffle(options);

        logger.info("Returning {} shuffled options for quiz ID: {}", options.size(), quizId);
        return options;
    }
    
 

}
