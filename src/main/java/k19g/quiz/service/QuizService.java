package k19g.quiz.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import k19g.quiz.DTO.QuizDTO;
import k19g.quiz.entity.Level;
import k19g.quiz.entity.Quiz;
import k19g.quiz.exception.LevelNotException;
import k19g.quiz.exception.QuizCategoriesNotFoundException;
import k19g.quiz.exception.QuizTitlesNotFoundException;
import k19g.quiz.exception.QuizTypesNotFoundException;
import k19g.quiz.exception.QuizzesNotFoundException;
import k19g.quiz.exception.QuizDeletionException;
import k19g.quiz.exception.QuizIdInvalidException;
import k19g.quiz.exception.QuizNotFoundException;
import k19g.quiz.repository.QuizRepository;
import k19g.quiz.utils.MiscellaneousUtils;

/**
 * <p>Service class for managing quiz-related operations.
 * This class encapsulates the business logic for fetching, saving, 
 * and manipulating quiz data, including categories, types, questions, and answers.</p>
 * 
 * <p><b>Author:</b> K19G</p>
 */
@Transactional
@Service
public class QuizService {

	private static final Logger logger= LoggerFactory.getLogger(QuizService.class);
	private final QuizRepository quizRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public QuizService(QuizRepository quizRepository, ObjectMapper objectMapper) {
        this.quizRepository = quizRepository;
        this.objectMapper = objectMapper;
    }


	/**
     * Retrieves all distinct quiz categories from the quiz repository.
     * 
     * @return a list of distinct quiz categories
     * @throws QuizCategoriesNotFoundException if no quiz categories are found in the repository
     */
    @Cacheable(value="quizCategories")
    public List<String> getAllCategories() {
        
    	logger.info("Fetching all distinct quiz categories.");

        List<String> quizCategories = quizRepository.findDistinctCategoryBy();

        MiscellaneousUtils.checkIfListIsEmpty(quizCategories, new QuizCategoriesNotFoundException());

        return quizCategories;
    }

    
    /**
     * Retrieves all distinct quiz types from the quiz repository.
     * 
     * @return a list of distinct quiz types
     * @throws QuizTypesNotFoundException if no quiz types are found in the repository
     */
    @Cacheable(value = "quizTypesCache")
    public List<String> getAllTypes() {
        
    	logger.info("Fetching all distinct quiz types.");

        List<String> quizTypes = quizRepository.findDistinctTypeBy();

        MiscellaneousUtils.checkIfListIsEmpty(quizTypes, new QuizTypesNotFoundException());

        return quizTypes;
    }
    
    
    /**
     * Retrieves the titles of all quiz questions from the quiz repository.
     * 
     * @return a list of all quiz question titles
     * @throws QuizTitlesNotFoundException if no quiz titles are found in the repository
     */
    @Cacheable(value = "quizTitlesCache")
    public List<String> getAllQuizQuestionTitle() {
        
    	logger.info("Fetching all quiz question titles.");

        List<String> quizTitles = quizRepository.findAllQuestionTitles();

        MiscellaneousUtils.checkIfListIsEmpty(quizTitles, new QuizTitlesNotFoundException());

        return quizTitles;
    }
    

    /**
     * Retrieves all quiz entries from the quiz repository.
     * 
     * @return a list of all quizzes
     * @throws QuizzesNotFoundException if no quizzes are found in the repository
     */
    @Cacheable(value = "getAllQuizsCache")
    public List<Quiz> getAllQuizs() {
    	
        logger.info("Fetching all quizzes.");

        List<Quiz> quizzes = quizRepository.findAll();

        MiscellaneousUtils.checkIfListIsEmpty(quizzes, new QuizzesNotFoundException());

        return quizzes;
    }

    
    /**
     * Persists a quiz into the repository.
     * If the quiz object has an ID, it will update the existing record. If the 
     * ID is null, a new record will be created.
     * 
     * @param quiz the quiz object to be saved or updated. Must not be null.
     * @throws QuizNotFoundException if the quiz object is null.
     */
    @CachePut(value = "quizCache", key = "#quiz.id")
    public boolean saveQuiz(Quiz quiz) {
    	
    	MiscellaneousUtils.checkIfEmpty(quiz, new QuizNotFoundException());
        
    	logger.info("Attempting to save quiz with title: {} and ID: {}", quiz.getQuestionTitle(), quiz.getId());

        try {
            quizRepository.save(quiz);

            logger.info("Quiz saved successfully with ID: {}", quiz.getId());
            return true;
            
        } catch (Exception e) {
            logger.error("Failed to save quiz: {}", e.getMessage(), e);
            
            return false;       
          
        }
    }
    

    /**
     * This method queries the repository to find a quiz with the specified ID. 
     * 
     * @param quizId the unique ID of the quiz to retrieve. Must not be null
     * @return an Optional containing the quiz if found, otherwise an empty Optional
     * @throws QuizIdInvalidException if the provided quizId is null
     * @throws QuizNotFoundException if the provided quizId is invalid or no quiz found that on that ID
     */
    @Cacheable(value = "quizCache", key = "#quizId")
    public Optional<Quiz> getQuiz(Integer quizId) {
    	
    	MiscellaneousUtils.checkIfEmpty(quizId, new QuizIdInvalidException());
        
        logger.info("Fetching quiz with ID: {}", quizId);
        
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        
        MiscellaneousUtils.checkIfEmpty(quiz, new QuizNotFoundException("No quiz found with ID: " + quizId));
        
        logger.info("Quiz found: {}", quiz.get().getQuestionTitle());

        return quiz;
    }

    
    /**
     * This method attempts to delete the quiz associated with the provided ID. 
     * 
     * @param id the unique ID of the quiz to be deleted. Must not be null
     * @return true if the quiz was successfully deleted; false otherwise
     * @throws QuizIdInvalidException if the provided ID is null
     * @throws QuizNotFoundException if the provided ID is invalid or no quiz found that on that ID
     * @throws QuizDeletionException if the quiz is unable to delete/remove
     * 
     */
    @CacheEvict(value = "quizCache", key = "#quizId")
    public boolean deleteQuizById(Integer quizId) {
    	
    	MiscellaneousUtils.checkIfEmpty(quizId, new QuizIdInvalidException());

        Long beforeQuizSize = quizRepository.count();
        
        logger.info("Attempting to delete quiz with ID: {}", quizId);

        Optional<Quiz> quiz = quizRepository.findById(quizId);

        MiscellaneousUtils.checkIfEmpty(quiz, new QuizNotFoundException("No quiz found with ID: " + quizId));

        try {
            quizRepository.deleteById(quizId);

            Long afterQuizSize = quizRepository.count();
            logger.info("Quiz deletion attempted. Size before deletion: {}, Size after deletion: {}",beforeQuizSize, afterQuizSize);

            boolean deletionSuccessful = !beforeQuizSize.equals(afterQuizSize);
            
            if (deletionSuccessful) {
                logger.info("Successfully deleted quiz with ID: {}", quizId);
            } else {
                logger.warn("No quiz found with ID: {} for deletion.", quizId);
            }

            return deletionSuccessful;

        } catch (Exception e) {
            logger.error("An error occurred while deleting quiz with ID: {}. Error: {}", quizId, e.getMessage(), e);
            throw new QuizDeletionException("Error occurred while deleting quiz with ID: " + quizId, e);
        }
    }
    
    
    /**
     * Retrieves random quizzes filtered by level, category, and type with randomize options.
     * The quizzes are fetched using pagination to limit the number of results.
     * 
     * @param level the difficulty level of the quizzes
     * @param category the category of the quizzes
     * @param type the type of the quizzes
     * @param numberOfQuestions the number of quizzes to fetch
     * @return a list of quizzes matching the specified criteria
     * @throws IllegalArgumentException if numberOfQuestions is invalid
     * @throws LevelNotException if level is invalid
     * @throws QuizzesNotFoundException if random quizzes is not found based on parameters
     */
    @Transactional
    public List<QuizDTO> getRandomQuizByLevelCategoryAndType(String level, String category, String type, int numberOfQuestions) {
       
    	if (numberOfQuestions <= 0) {
            throw new IllegalArgumentException("Number of questions must be positive.");
        }

        logger.info("Fetching {} random questions with level: {}, category: {}, type: {}", numberOfQuestions, level, category.isEmpty()?"All Category":category, type.isEmpty()?"All Type":type);


        Level enumLevel;
        try {
            enumLevel = Level.valueOf(level.toUpperCase());  
        } catch (IllegalArgumentException e) {
            logger.error("Invalid level provided: {}. Allowed values: EASY, MEDIUM, HARD, EXPERT.", level, e);
            throw new LevelNotException("Invalid level provided. Allowed values: EASY, MEDIUM, HARD, EXPERT.");
        }

        Pageable pageable = PageRequest.of(0, numberOfQuestions);  

        Page<Quiz> paginatedQuizzes  = quizRepository.findByLevelCategoryAndType(pageable, enumLevel.name(), category, type);
        
        if(paginatedQuizzes==null || paginatedQuizzes.isEmpty())
        	throw new QuizzesNotFoundException(
        		    "No random quizzes based on " + enumLevel.name() + ", " 
        		    + (category.isEmpty() ? "All Category" : category) + ", and " 
        		    + (type.isEmpty() ? "All Type" : type) + " with pagination found."
        		);
        
        List<QuizDTO> quizzesDTO=convertAndRandomizeQuizzes(paginatedQuizzes.getContent());

        logger.info("Fetched {} random quizzes matching criteria.", paginatedQuizzes.getContent().size());
        
        return quizzesDTO;
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
    @Cacheable(value = "answersCache", key = "#questionId")
    public List<String> getAnswerById(Integer questionId) {
       
        if (questionId == null || questionId <= 0) {
            throw new IllegalArgumentException("Invalid question ID provided. Question ID must be a positive integer.");
        }

        logger.info("Fetching answers for question ID: {}", questionId);
        
        List<String> AnsList=quizRepository.findAnswersByQuestionId(questionId);
        
        MiscellaneousUtils.checkIfListIsEmpty( AnsList, new QuizNotFoundException("Quiz not found with ID: "+questionId));
        
        return AnsList;
    }


    /**
     * Retrieves all distinct quiz levels from the quiz repository.
     * 
     * This method is responsible for fetching a list of distinct levels 
     * 
     * @return a list of distinct quiz levels
     * @throws LevelNotException if no levels are found in the repository
     */
    @Cacheable(value = "distinctLevelsCache")
    public List<Level> getAllDistinctLevels() {
        
    	logger.info("Fetching all distinct quiz levels.");
        
        List<Level> distinctLevels  = quizRepository.findDistinctLevels();
       
        MiscellaneousUtils.checkIfListIsEmpty(distinctLevels, new LevelNotException());
        
        List<Level> sortedLevels = Arrays.stream(Level.values())
            .filter(level -> distinctLevels.contains(level))
            .collect(Collectors.toList());
        
        MiscellaneousUtils.checkIfListIsEmpty(sortedLevels, new LevelNotException("No distinct levels found in the quiz repository."));

        logger.info("Fetched distinct quiz levels: {}", sortedLevels);
        
        return sortedLevels;
    }
    
    
    /**
     * <p>Reads a JSON file containing a list of quizzes and saves them to the database.</p>
     * 
     * <p>This method accepts a <code>MultipartFile</code> representing the uploaded JSON file,
     * converts it into a list of <code>Quiz</code> objects, and then saves them using 
     * the <code>quizRepository</code>.</p>
     *
     * @param file the <code>MultipartFile</code> containing the JSON data for quizzes
     * @throws IOException if an I/O error occurs while reading the file or parsing the JSON
     * @throws QuizzesNotFoundException if after parsing  quizzes are empty.
     */
    public void saveJSONQuiz(MultipartFile file) throws IOException {
    	
    	List<Quiz> quizzes = objectMapper.readValue(file.getInputStream(), new TypeReference<List<Quiz>>() {});
    	
    	MiscellaneousUtils.checkIfListIsEmpty(quizzes, new QuizzesNotFoundException());
    	
    	quizRepository.saveAll(quizzes);
    }
    
    /**
     * Converts a list of Quiz objects into QuizDTO objects and randomizes their options.
     *
     * @param quizzes The list of Quiz objects to convert and shuffle.
     * @return A list of QuizDTO objects with shuffled options.
     */
    private List<QuizDTO> convertAndRandomizeQuizzes(List<Quiz> quizzes) {
        
    	List<QuizDTO> quizDTOs = new ArrayList<>();
        
        for (Quiz quiz : quizzes) {
        	
            QuizDTO quizDto = QuizDTO.convertToDTO(quiz);
            
            if (quizDto.getOptions() != null) {
                List<String> shuffledOptions = new ArrayList<>(quizDto.getOptions());
                Collections.shuffle(shuffledOptions);
                quizDto.setOptions(shuffledOptions);
            }
            quizDTOs.add(quizDto);
        }
        return quizDTOs;
    }


}
