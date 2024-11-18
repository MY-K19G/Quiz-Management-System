package k19g.quiz.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

import k19g.quiz.DTO.QuizDTO;
import k19g.quiz.exception.MissingSessionAttributeException;
import k19g.quiz.exception.QuizCategoriesNotFoundException;
import k19g.quiz.exception.QuizTypesNotFoundException;
import k19g.quiz.exception.QuizzesNotFoundException;
import k19g.quiz.service.QuizService;
import k19g.quiz.utils.MiscellaneousUtils;

/**
 * Controller for managing access to quiz-related views and actions.
 * 
 * <p>This controller handles incoming HTTP requests related to quiz access,
 * such as displaying the quiz creation page or accessing the quiz results page.</p>
 * 
 * <p><strong>Author:</strong> K19G</p>
 */
@Controller
public class QuizAccessController{
	
	private static final Logger logger = LoggerFactory.getLogger(QuizAccessController.class);
	
    private final QuizService quizService;
    
    @Autowired
	private QuizAccessController(QuizService quizService){
		this.quizService=quizService;
	}
	
	/**
	 * Initiates the quiz setup by retrieving all available categories and types from the quiz service.
	 * <p>
	 * This method is mapped to the root URL ("/"). It adds the list of all quiz categories and types to the 
	 * model, which will be used in the view to allow users to select options for their quiz.
	 * </p>
	 *
	 * @param session the HTTP session used to store quiz-related information.
	 * @return a {@link ModelAndView} object containing the model attributes and the view name for the 
	 *         quiz assessment details page.
	 */
	@GetMapping("/")
	public ModelAndView initiateQuizSetup(HttpSession session,HttpServletResponse response) {
	    
		ModelAndView mav=new ModelAndView();
		
		List<String> categories =  quizService.getAllCategories();
		List<String> types = quizService.getAllTypes();
		
		MiscellaneousUtils.checkIfListIsEmpty(categories, new QuizCategoriesNotFoundException("Quiz categories could not be found."));
		MiscellaneousUtils.checkIfListIsEmpty(types, new QuizTypesNotFoundException("Quiz type could not be found."));
		
	    mav.addObject("allCategorys", categories);
	    mav.addObject("allTypes", types);
	    
	    logger.info("Fetched categories: {} and types: {}", categories, types);
	    session.setAttribute("quiz_access", false);
	    
	    mav.setViewName("app/quizAssessmentDetails");
	    return mav;
	}

	
	
	/**
	 * Processes the quiz setup by retrieving random quiz questions based on the user's inputs.
	 * <p>
	 * This method is mapped to the "/quiz" URL and expects various parameters related to the quiz setup.
	 * It validates the input parameters and retrieves a list of generated questions. The method also 
	 * manages session attributes to store quiz-related data and sets the view for the quiz participant page.
	 * </p>
	 *
	 * @param name         the user name of the participant.
	 * @param noOfQuestion the number of questions to be generated for the quiz.
	 * @param level        the difficulty level of the quiz (easy, medium, hard, etc.).
	 * @param category     the category of the quiz questions.
	 * @param type         the type of quiz questions (programming, theory, etc.).
	 * @param time         the total time allocated for the quiz in minutes.
	 * @param session      the HTTP session used to store quiz-related information.
	 * @param response     the HTTP response used to set cache control headers.
	 * @return a {@link ModelAndView} object containing the model attributes and the view name for the quiz participant page.
	 */
	@PostMapping("/processQuizSetup")
	public ModelAndView processQuizSetup(
	        @RequestParam("username") String name,
	        @RequestParam("questions") Integer noOfQuestion,
	        @RequestParam("level") String level,
	        @RequestParam("category") String category,
	        @RequestParam("type") String type,
	        @RequestParam("time") Integer time,
	        HttpSession session,
	        HttpServletResponse response) {
		
		ModelAndView mav=new ModelAndView();
		 
	    logger.info("Processing quiz setup for user: {}", name);
	   
	    validateQuizParameters(name, noOfQuestion, time, level);

	    logger.info("Generating questions for category: {}, type: {}, level: {}",
	    		category.isEmpty()?"All Category":category,
	    		 type.isEmpty()?"All Type":type, level);

	    List<QuizDTO> generatedQuiz  = quizService.getRandomQuizByLevelCategoryAndType(level, category, type, noOfQuestion);
	    
	    MiscellaneousUtils.checkIfListIsEmpty(generatedQuiz,
	    		new QuizzesNotFoundException("Please try changing requirments.\n or \nPlease try again later."));
	    
	    session.setAttribute("fromQuizPage", true);
	    session.setAttribute("username", name);
	    session.setAttribute("noOfQuestion", noOfQuestion);
	    session.setAttribute("level", level);
	    session.setAttribute("category", category);
	    session.setAttribute("type", type);
	    session.setAttribute("time", time);
	    session.setAttribute("generatedQuiz", generatedQuiz);

	    session.setAttribute("quiz_access", true);
	    
	    logger.info("Quiz setup completed for user: {} with {} questions", name, noOfQuestion);
	    
	    mav.setViewName("redirect:/quiz");
	    
	    return mav;
	}
	
	/**
	 * <p>Validates the parameters for creating a quiz. Helper method for <b>{@link #processQuizSetup}</b> that checks the provided quiz name, number of questions, time, 
	 * and level meet the necessary conditions.</p>
	 *
	 * @param name The name of the quiz. Cannot be null or empty.
	 * @param noOfQuestion The number of questions in the quiz. Must be a positive integer greater than 0.
	 * @param time The time allocated for the quiz. Must be a positive integer greater than 0.
	 * @param level The difficulty level of the quiz. Cannot be null or empty.
	 * 
	 * @throws IllegalArgumentException If any of the parameters are invalid, an <code>IllegalArgumentException</code> is thrown.
	 */
	private void validateQuizParameters(String name, Integer noOfQuestion, Integer time, String level) {
	    if (name == null || name.isEmpty()) {
	        logger.error("Username cannot be empty");
	        throw new IllegalArgumentException("Username cannot be empty.");
	    }

	    if (noOfQuestion == null || noOfQuestion <= 0) {
	        logger.error("Invalid number of questions: {}", noOfQuestion);
	        throw new IllegalArgumentException("Number of questions must be a positive integer.");
	    }
	    else if(noOfQuestion < 1 || noOfQuestion > 30) {
	    	 logger.error("Number of questions must be less than 30.");
		     throw new IllegalArgumentException("Number of questions must be between 1 and 30.");
	    }

	    if (time == null || time <= 0) {
	        logger.error("Invalid quiz time: {}", time);
	        throw new IllegalArgumentException("Time must be a positive integer.");
	    }
	    else if(time < 1 || time > 60) {
	    	 logger.error("Number of time(min) must be less than or equls to 60");
		     throw new IllegalArgumentException("Number of time(min) must be between 1 and 60.");
	    }

	    if (level == null || level.isEmpty()) {
	        logger.error("Level cannot be empty {}", level);
	        throw new IllegalArgumentException("Level cannot be empty.");
	    }
	}

		
	/**
	 * Controller method to handle the "/quiz" endpoint for retrieving and displaying a quiz.
	 * 
	 * <p>This method retrieves quiz questions and total time from the session, 
	 * adds them as model attributes, and then directs to the quiz participant page.
	 * </p>
	 * 
	 * @param session the HTTP session that contains quiz data, such as questions and time.
	 * @param response the HTTP response, used to customize response attributes if needed.
	 * @return ModelAndView object that holds view name and quiz data to be displayed.
	 */
	@GetMapping("/quiz")
	public ModelAndView showQuiz(HttpSession session,HttpServletResponse response) {
		
		ModelAndView mav= new ModelAndView();

		Integer total_time =(Integer) session.getAttribute("time");
	    
		List<QuizDTO> generatedQuiz = (List<QuizDTO>)session.getAttribute("generatedQuiz");
		
	    MiscellaneousUtils.checkIfListIsEmpty(generatedQuiz,
	    		new QuizzesNotFoundException("'generatedQuiz' session is not found.\n or \nPlease try again."));
		 
	    mav.addObject("Quiz_questions", generatedQuiz);
		mav.addObject("total_time", total_time);
		
		mav.setViewName("app/quizParticipantPage");

		logger.info("Quiz participant page prepared successfully.");

		return mav;
	}
	
	/**
	 * Handles the retrieval of the quiz results for the participant.
	 * <p>
	 * This method is mapped to the "/result" URL and retrieves the quiz results based on the user's attempts.
	 * It calculates the total correct and wrong answers and prepares the model for displaying the results on the results dash board.
	 * </p>
	 *
	 * @param session the HTTP session used to retrieve quiz-related data.
	 * @return a {@link ModelAndView} object containing the model attributes and the view name for the quiz results dash board.
	 */
	@GetMapping("/result")
	public ModelAndView evaluationResult(HttpSession session,HttpServletResponse response) {

		 session.setAttribute("quiz_access", false);
		
		 ModelAndView mav=new ModelAndView();
		 
		 logger.info("Starting evaluation of quiz results.");

	    Boolean fromQuizPage = (Boolean) session.getAttribute("fromQuizPage");
	    
	    if (fromQuizPage == null || !fromQuizPage) {
	    	logger.warn("Access denied: User did not navigate from the quiz page.");
	    	mav.addObject("errorMessage", "Access denied: User did not navigate from the quiz page.");
	    	mav.setViewName("403");
	    	return mav;
	    }
	    
		Map<Integer, List<String>> participantQuizData = (Map<Integer, List<String>>) session.getAttribute("attemptQuiz");

	    if (participantQuizData == null || participantQuizData.isEmpty()) {
	    	logger.error("Participant quiz data is not available in session.");
	    	mav.setViewName("resultNotAvailable");
	    	return mav;
	    }
	    
	    List<List<Integer>> correctAnsListArr= new ArrayList<List<Integer>>();
	    	
	    String	name = getSessionAttribute(session, "username", String.class);
        Integer noOfQuestion = getSessionAttribute(session, "noOfQuestion", Integer.class);
        String 	level = getSessionAttribute(session, "level", String.class);
        String 	category = getSessionAttribute(session, "category", String.class);
        String 	type = getSessionAttribute(session, "type", String.class);
        String 	total_time = getSessionAttribute(session, "time", Integer.class).toString();
        List<QuizDTO> generatedQuiz = getSessionAttribute(session, "generatedQuiz", List.class);
	    
	    category = (category == null || category.isEmpty()) ? "All Category" : category;
	    type = (type == null || type.isEmpty()) ? "All Type" : type;
	    total_time = (total_time != null) ? total_time : "N/A";
	    
	    Integer totalCorrectAns = 0;
	    Integer totalWrongAns = 0;
	    
	     for (Entry<Integer, List<String>> attempt : participantQuizData.entrySet()) 
	        {
	            List<String> correctAns = quizService.getAnswerById(attempt.getKey());
	        
	            boolean booleanAnswer=areStringListsEqualIgnoreOrder(correctAns,attempt.getValue());
	            
	            if (booleanAnswer) {		
	                totalCorrectAns++;
	            } else {
	                totalWrongAns++;
	            }
	            
	        }   
	        
	     logger.info(
	    		    "Evaluation completed: {} correct answers and {} incorrect answers.",
	    		    totalCorrectAns,
	    		    totalWrongAns
	    		);
	        
	        for(int i=0; i<generatedQuiz.size(); i++) {
	        	
	        	List<Integer> correctIntegerArray = findMatchingIndexes(
	        		    generatedQuiz.get(i).getAnswers(),
	        		    generatedQuiz.get(i).getOptions()
	        		);
	        	
	        	correctAnsListArr.add(correctIntegerArray);
	        }
	        session.setAttribute("correctAnsInt", correctAnsListArr);
	        
	      
	      mav.addObject("username", name);
	      mav.addObject("noOfQuestion", noOfQuestion);
	      mav.addObject("level", level);
	      mav.addObject("category", category);
	      mav.addObject("type", type);
	      mav.addObject("selectedTime", total_time);
	      mav.addObject("totalAttemptQuestion", participantQuizData.size());
	      mav.addObject("totalCorrectAns", totalCorrectAns);
	      mav.addObject("totalWrongAns", totalWrongAns);

	      mav.setViewName("app/quizResultsDashboard");

	      logger.info("Quiz results dashboard prepared successfully for user: {}", name);

	    return mav;
	}
	
	/**
	 * <p>Helper method for <b>{@link #evaluationResult}</b> that retrieves an attribute from the HTTP session 
	 * and ensures it is of the expected type. If the attribute is not found
	 * or is of an unexpected type, a MissingSessionAttributeException or a {@link ClassCastException} is thrown.</p>
	 *
	 * @param session The {@link HttpSession} from which the attribute will be retrieved.
	 * @param attributeName The name of the session attribute to retrieve.
	 * @param type The expected type of the session attribute.
	 * @param <T> The type of the session attribute.
	 * 
	 * @return The session attribute, cast to the expected type.
	 * 
	 * @throws MissingSessionAttributeException If the session attribute is missing.
	 * @throws ClassCastException If the session attribute is not of the expected type.
	 */
	private <T> T getSessionAttribute(HttpSession session, String attributeName, Class<T> type) {
	   
		Object attribute = session.getAttribute(attributeName);
	   
		if (attribute == null) {
	    	logger.error(attributeName+ " is missing");
	        throw new MissingSessionAttributeException(attributeName+ "is missing");  // Throw custom exception if not found
	    }
	    
	    if (!type.isInstance(attribute)) {
	    	logger.error("Session attribute '" + attributeName + "' is not of the expected type " + type.getName());
	        throw new ClassCastException("Session attribute '" + attributeName + "' is not of the expected type " + type.getName());
	    }
	    
	    return type.cast(attribute);
	}

	/**
	 * <p>Finds the indexes in the <code>options</code> list where elements match those in the <code>Correct answer</code> list.</p>
	 * 
	 * @param CorrAns the list of correct answers to match.
	 * @param options the list of answer options to search through.
	 * @return a list of integer indexes from <code>options</code> where matches were found.
	 */
	 private  List<Integer> findMatchingIndexes(List<String> CorrAns, List<String> options) {
	        List<Integer> matchingIndexes = new ArrayList<>();
	        
	        for (int i = 0; i < CorrAns.size(); i++) {
	            String str1 = CorrAns.get(i);
	            
	            for(int j=0;j<options.size();j++) {
	            	
	            	 if (str1.equals(options.get(j))) {
		                    matchingIndexes.add(j);  
		                    break;  
		                }
	            }
	        }
	        logger.info("Completed findMatchingIndexes. Matching indexes: {}", matchingIndexes);
	        return matchingIndexes;
	    }
	
	
	 /**
	  * <p>Checks if two lists of strings are equal, ignoring order. If both lists have the same elements in any order, 
	  * the method returns <code>true</code>. Otherwise, it returns <code>false</code>.</p>
	  * 
	  * @param list1 the first list of strings to compare.
	  * @param list2 the second list of strings to compare.
	  * @return <code>true</code> if both lists contain the same elements in any order, <code>false</code> otherwise.
	  */
    private static boolean areStringListsEqualIgnoreOrder(List<String> list1, List<String> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        Collections.sort(list1);
        Collections.sort(list2);
        
        boolean isEqual = list1.equals(list2);
        
        logger.info("Completed areStringListsEqualIgnoreOrder. Are lists equal (ignoring order)? {}", isEqual);
        return isEqual;
    }
    
    
    /**
     * <p>Controller method to handle access-denied errors.
     * which is intended to display a "403 Forbidden" page to the user.</p>
     * 
     * <p>When users attempt to access a restricted resource without the proper
     * permissions, they are redirected to this page. The returned view name "403"</p> 
     * 
     * 
     * @return String representing the view name ("403") for the access-denied page.
     */
    @RequestMapping("/403")
    public String accessDenied() {
        return "403"; 
    }
	
    @RequestMapping("/favicon.ico")
    @ResponseStatus(HttpStatus.NO_CONTENT)  
    public void returnNoFavicon() {
        //  204 No Content 
    }
}