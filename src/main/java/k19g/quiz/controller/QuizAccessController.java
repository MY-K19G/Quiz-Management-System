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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import k19g.quiz.entity.Quiz;
import k19g.quiz.error.MissingSessionAttributeException;
import k19g.quiz.service.QuizService;

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

    /**
     * The QuizService instance used to retrieve quiz categories and types for the quiz setup.
     */
    @Autowired
    private QuizService quizService;
	
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
		List<String> categories = quizService.getAllCategories();
		List<String> types = quizService.getAllTypes();
		    
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
	 * @param name         the username of the participant.
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
		 
		// Log that the method is being executed
	    logger.info("Processing quiz setup for user: {}", name);
	    
	    // Validate the inputs
	    if (name == null || name.isEmpty()) {
	    	logger.error("Username cannot be empty");
	        throw new IllegalArgumentException("Username cannot be empty");
	    }
	    if (noOfQuestion == null || noOfQuestion <= 0) {
	    	 logger.error("Invalid number of questions: {}", noOfQuestion);
	        throw new IllegalArgumentException("Number of questions must be a positive integer");
	    }
	    if (time == null || time <= 0) {
	    	logger.error("Invalid quiz time: {}", time);
	        throw new IllegalArgumentException("Time must be a positive integer");
	    }
	    if (level == null || level.isEmpty()) {
	    	logger.error("Level cannot be empty {}", level);
	        throw new IllegalArgumentException("Level cannot be empty");
	    }

	    logger.info("Generating questions for category: {}, type: {}, level: {}", category, type, level);

	    List<Quiz> generatedQuiz = quizService.getRandomQuizByLevelCategoryAndType(level, category, type, noOfQuestion);
	    
	    // shuffling options for quiz
	    for(Quiz quiz:generatedQuiz) {
	    List<String> randomizedOptions=quizService.getRandomOptionsForQuiz(quiz.getId());
	    quiz.setOptions(randomizedOptions);
        logger.debug("Generated quiz: {} with randomized options: {}", quiz.getId(), randomizedOptions);
	    }
	    
	    
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
	    List<Quiz> generatedQuiz =(List<Quiz>)session.getAttribute("generatedQuiz");
		 
	    mav.addObject("Quiz_questions", generatedQuiz);
		mav.addObject("total_time", total_time);
		mav.setViewName("app/quizParticipantPage");

		logger.info("Quiz participant page prepared successfully.");

		return mav;
	}
	
	/**
	 * Handles the retrieval of the quiz results for the participant.
	 * <p>
	 * This method is mapped to the "/result" URL and retrieves the quiz results based on the user's attempts stored in the session.
	 * It calculates the total correct and wrong answers and prepares the model for displaying the results on the results dashboard.
	 * </p>
	 *
	 * @param session the HTTP session used to retrieve quiz-related data.
	 * @return a {@link ModelAndView} object containing the model attributes and the view name for the quiz results dashboard.
	 */
	@GetMapping("/result")
	public ModelAndView evaluationResult(HttpSession session,HttpServletResponse response) {

		 session.setAttribute("quiz_access", false);
		
		 ModelAndView mav=new ModelAndView();
		 
		 // Log the start of the result processing
		 logger.info("Starting evaluation of quiz results.");

		// Validate session attributes
	    Boolean fromQuizPage = (Boolean) session.getAttribute("fromQuizPage");
	    
	    if (fromQuizPage == null || !fromQuizPage) {
	    	logger.warn("Access denied: User did not navigate from the quiz page.");
	    	mav.setViewName("403");
	    }
	    
	    @SuppressWarnings("unchecked")	//it contain user given quiz Entity
		Map<Integer, List<String>> participantQuizData = (Map<Integer, List<String>>) session.getAttribute("attemptQuiz");


	    if (participantQuizData == null || participantQuizData.isEmpty()) {
	    	logger.error("Participant quiz data is not available in session.");
	        throw new MissingSessionAttributeException("Participant quiz data is not available.");
	    }
	    
	    List<List<Integer>> listArr= new ArrayList<List<Integer>>();

	    String name = (String) session.getAttribute("username");
	    int noOfQuestion = (int) session.getAttribute("noOfQuestion");
	    String level = (String) session.getAttribute("level");
	    String category = (String) session.getAttribute("category");
	    String type = (String) session.getAttribute("type");
	    String total_time = session.getAttribute("time").toString();
	    List<Quiz> generatedQuiz =(List<Quiz>)session.getAttribute("generatedQuiz");
	    
	    // Set default values if necessary
	    category = (category == null || category.isEmpty()) ? "All Category" : category;
	    type = (type == null || type.isEmpty()) ? "All Type" : type;
	    total_time = (total_time != null) ? total_time : "N/A";
	    
	 // Initialize counters
	    int totalCorrectAns = 0;
	    int totalWrongAns = 0;
	    
	        // Evaluate participant's answers
	        for (Entry<Integer, List<String>> attempt : participantQuizData.entrySet()) 
	        {
	            List<String> correctAns = quizService.getAnswerById(attempt.getKey());
	        
	            boolean booleanAnswer=areStringListsEqualIgnoreOrder(correctAns,attempt.getValue());
	            
	            if (booleanAnswer) {		//first recive string attemp answer and correct ans verify
	                totalCorrectAns++;
	            } else {
	                totalWrongAns++;
	            }
	            
	        }
	        
	        logger.info("Evaluation completed: {} correct answers and {} incorrect answers.", totalCorrectAns, totalWrongAns);
	        
	        for(int i=0;i<generatedQuiz.size();i++) {
	        	
	        	//generate attemp option using loop	
	        	List<Integer> correctIntegerArray=  findMatchingIndexes(generatedQuiz.get(i).getAnswers(),generatedQuiz.get(i).getOptions());
	        	listArr.add(correctIntegerArray);
	        	
	        }
	        session.setAttribute("correctAnsInt", listArr);
	        
	      
	   // Prepare the model attributes for the results view
	      mav.addObject("username", name);
	      mav.addObject("noOfQuestion", noOfQuestion);
	      mav.addObject("level", level);
	      mav.addObject("category", category);
	      mav.addObject("type", type);
	      mav.addObject("selectedTime", total_time);
	      mav.addObject("totalAttemptQuestion", participantQuizData.size());
	      mav.addObject("totalCorrectAns", totalCorrectAns);
	      mav.addObject("totalWrongAns", totalWrongAns);

	      // Set the view name for the quiz results dashboard
	      mav.setViewName("app/quizResultsDashboard");

	      logger.info("Quiz results dashboard prepared successfully for user: {}", name);

	    return mav;
	}

	
	/**
	 * <h3>findMatchingIndexes</h3>
	 * <p>Finds the indexes in the <code>options</code> list where elements match those in the <code>CorrAns</code> list.</p>
	 * <p><strong>Usage:</strong> Loops through each element in <code>CorrAns</code>, checking if it appears in <code>options</code>, 
	 * and if found, the index of the match in <code>options</code> is added to the result list.</p>
	 * 
	 * @param CorrAns the list of correct answers to match.
	 * @param options the list of answer options to search through.
	 * @return a list of integer indexes from <code>options</code> where matches were found.
	 */
	 public  List<Integer> findMatchingIndexes(List<String> CorrAns, List<String> options) {
	        List<Integer> matchingIndexes = new ArrayList<>();
	        
	        // Loop through the first list and compare it with each element of the second list
	        for (int i = 0; i < CorrAns.size(); i++) {
	            String str1 = CorrAns.get(i);
	            
	            // Check if the current string in list1 exists in list2
	            for(int j=0;j<options.size();j++) {
	            	
	            	 if (str1.equals(options.get(j))) {
		                    matchingIndexes.add(j);  // Add index of the matching string
		                    break;  // Only the first occurrence from list1 is considered
		                }
	            }
	            
	        }
	        logger.info("Completed findMatchingIndexes. Matching indexes: {}", matchingIndexes);
	        return matchingIndexes;
	    }
	
	
	 /**
	  * <h3>areStringListsEqualIgnoreOrder</h3>
	  * <p>Checks if two lists of strings are equal, ignoring order. If both lists have the same elements in any order, 
	  * the method returns <code>true</code>. Otherwise, it returns <code>false</code>.</p>
	  * <p><strong>Note:</strong> Both lists are sorted before comparison.</p>
	  * 
	  * @param list1 the first list of strings to compare.
	  * @param list2 the second list of strings to compare.
	  * @return <code>true</code> if both lists contain the same elements in any order, <code>false</code> otherwise.
	  */
    public static boolean areStringListsEqualIgnoreOrder(List<String> list1, List<String> list2) {
        // If the lists have different sizes, they cannot be equal
        if (list1.size() != list2.size()) {
            return false;
        }

        // Sort both lists and compare
        Collections.sort(list1);
        Collections.sort(list2);
        
        boolean isEqual = list1.equals(list2);
        
        logger.info("Completed areStringListsEqualIgnoreOrder. Are lists equal (ignoring order)? {}", isEqual);
        return isEqual;
    }
    
	
}