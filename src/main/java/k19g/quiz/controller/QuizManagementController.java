package k19g.quiz.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import k19g.quiz.entity.Quiz;
import k19g.quiz.service.QuizService;

/**
 * <h1>Quiz Management Controller</h1>
 * <p>This controller manages the main operations related to quizzes in the application,
 * including creation, updating, editing, and deletion. Each method provides functionality
 * to handle HTTP requests for different quiz management operations.</p>
 */
@Controller
public class QuizManagementController {
	
    private static final Logger logger = LoggerFactory.getLogger(QuizManagementController.class);
	
	@Autowired
	private QuizService quizService;

	@Autowired
	private ModelAndView mav;

	
	/**
     * Displays the page for creating a quiz.
     * 
     * <p>This method handles the HTTP GET request for the quiz creation page.
     * It returns the view name for the CreateQuiz page.</p>
     * 
     * @return the name of the view for creating a quiz
     */
    @GetMapping("/create")
    public String showCreatepage() {
    	 logger.info("Navigating to Create Quiz page.");
        return "CreateQuiz"; // view name for quiz creation
    }
	
    
	/**
	 * Controller method to receive and save quiz questions.
	 * 
	 * <p>This method handles the HTTP POST request to save a quiz question.
	 * It processes the submitted data, differentiating between theory and programming questions,
	 * and saves the quiz to the database.</p>
	 * 
	 * @param quiz the Quiz object containing quiz details
	 * @param theoryQ the theory question submitted by the user
	 * @param progQ the programming question submitted by the user
	 * @param type the type of the question, defaults to "Theory"
	 * @return a ModelAndView object redirecting to the quiz creation page
	 */
	@PostMapping("/save_quiz")
	public ModelAndView addQuizPage(@ModelAttribute Quiz quiz, 
										@RequestParam("theory_question") String theoryQ,
	                                    @RequestParam("programming_question") String progQ,
	                                    @RequestParam(value = "type", defaultValue = "Theory") String type,
	                                    HttpSession session) {

	    // Validate input for theory and programming questions
	    if (theoryQ == null || theoryQ.trim().isEmpty()) {
	        // If theory question is empty, set the programming question as the question code
	        quiz.setQuestionCode(progQ);
	    } else {
	        // If theory question is provided, set it as the question title
	        quiz.setQuestionTitle(theoryQ);
	    }

	    // Set the type of question (Theory/Programming)
	    quiz.setType(type);
	    
	    
	    List<String> answerList = quiz.getAnswers().stream()
	    	    .map(answerID -> quiz.getOptions().get(Integer.parseInt(answerID)))
	    	    .filter(Objects::nonNull) // Ensure the answer is not null
	    	    .filter(ans -> !ans.trim().isEmpty()) // Ensure the answer is not empty
	    	    .collect(Collectors.toList());

	    quiz.setAnswers(answerList);

	    
	    // Log the received quiz object for debugging purposes
	    logger.debug("Prepared quiz object: {}", quiz);
	    
	    // Save the quiz object to the database
	    boolean isInserted = quizService.saveQuiz(quiz); // Get success status
	    session.setAttribute("isQuizInserted", isInserted);
	    
        logger.info("Quiz saved status: {}", isInserted);
	    // Redirect to the quiz creation page after saving
	    mav.setViewName("redirect:/create");
	    return mav;
	}
	
	

	/**
	 * Controller method to display the quiz update page.
	 * 
	 * <p>This method handles the HTTP GET request for the quiz update page.
	 * It retrieves necessary data from the quiz service and adds it to the ModelAndView object
	 * for rendering the update quiz interface.</p>
	 * 
	 * @param session the HTTP session used to store session attributes
	 * @return a ModelAndView object containing the data for the update quiz page
	 */
	@GetMapping("/update")
	public ModelAndView quizUpdatePage(HttpSession session) { 
	    // Log that the update page method is being executed along with session information
        logger.info("Entering quizUpdatePage method. Session ID: {}", session.getId());
	    
	    ModelAndView mav = new ModelAndView();

	    // Retrieve data from the quiz service
	    mav.addObject("allCategories", quizService.getAllCategories());
	    mav.addObject("allQuestions", quizService.getAllQuizQuestionTitle());
	    mav.addObject("allTypes", quizService.getAllTypes());
	    mav.addObject("allLevels", quizService.getAllDistinctLevels());
	    mav.addObject("QuestionEntity", quizService.getAllQuizs());

	    // Set session attribute to indicate the user is on the update page
	    session.setAttribute("fromUpdatePage", true);

	    // Set the view name for the update quiz page
	    mav.setViewName("updateQuiz");
        logger.info("Quiz update page data prepared.");
	    return mav;
	}

	
	
	/**
	 * Controller method to display the edit page for a quiz question.
	 * 
	 * <p>This method retrieves the details of a quiz question based on the provided question ID,
	 * and prepares the edit page for the user. If the question ID is not provided or is invalid,
	 * appropriate error messages will be displayed.</p>
	 *
	 * @param questionId the ID of the quiz question to be edited
	 * @param session the HTTP session for storing user-related attributes
	 * @return a ModelAndView object for the edit quiz page or an error message
	 */
	@GetMapping("/edit")
	public ModelAndView editPage(@RequestParam(value = "questionId", required = false) Integer questionId, 
	                              HttpSession session) {
	    ModelAndView mav = new ModelAndView();

	    // Log the start of the edit page request
        logger.info("Received request to edit quiz. Question ID: {}", questionId);
	    
	    Boolean fromUpdatePage = (Boolean) session.getAttribute("fromUpdatePage");
	    
	    if (questionId == null) {
	        // Log an error for missing question ID
            logger.error("No question ID provided.");
	    } else {
	        // Fetch the quiz question using the provided ID
	        Optional<Quiz> optionalQuiz = quizService.getQuiz(questionId);
	        List<String> answerList=quizService.getAnswerById(questionId);
	        List<Integer> answerIntList=convertIntAnswerList(answerList,optionalQuiz.get().getOptions());
	        
	        if (optionalQuiz.isPresent()) {
	            mav.addObject("QuestionEntity", optionalQuiz.get());
	            mav.addObject("answers", answerIntList);
	            mav.setViewName("editQuiz");
	        } else {
	            // Log an error for invalid question ID
                logger.error("No quiz found for Question ID: {}", questionId);
	        }
	    }
	    
	    // Check if the request is coming from the update page
	    if (fromUpdatePage == null || !fromUpdatePage) {
	    	mav.addObject("homepage_url", "/update");
	        mav.setViewName("403"); // Access denied page
            logger.warn("Access denied. User tried to edit quiz without coming from update page.");
	    }

	    return mav;
	}
	
	private List<Integer> convertIntAnswerList(List<String> answerList, List<String> options) {
        logger.debug("Converting answer list: {} with options: {}", answerList, options);
		
		List<Integer> intList =new ArrayList<Integer>();
		for(int i=0;i<answerList.size();i++) {
			for(int j=0;j<options.size();j++) {
				if(answerList.get(i).equals(options.get(j))) {
					intList.add(j);
					break;
				}
			}
		}
        logger.debug("Converted answer indices: {}", intList);
		return intList;
	}




	@PostMapping("/update_quiz")  //why is he here
	public ModelAndView updateQuiz(@ModelAttribute Quiz quiz, 
			@RequestParam("theory_question") String theoryQ,
			@RequestParam("programming_question") String progQ,
			@RequestParam(value = "type", defaultValue = "Theory") String type) {

        logger.info("Updating quiz with TheoryQ: {}, ProgQ: {}, Type: {}", theoryQ, progQ, type);

			if("Theory".equalsIgnoreCase(type)) {
				quiz.setQuestionTitle(theoryQ);
				quiz.setType(type);
				
			}else {
				quiz.setQuestionCode(progQ);
			}
			quizService.saveQuiz(quiz);
			
	        logger.info("Quiz updated: {}", quiz);
	        
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/update");
			return mav;
	}
	
	




	/**
	 * Controller method to display the quiz deletion page.
	 * 
	 * <p>This method handles the HTTP GET request to display the quiz deletion page.
	 * It populates the model with all available categories, questions, and types,
	 * allowing the user to select which quiz to delete.</p>
	 *
	 * @return a ModelAndView object containing the view for deleting quizzes
	 */
	@GetMapping("/delete")
	public ModelAndView quizDeletePage() {
	    // Log the action of accessing the delete quiz page
        logger.info("Accessing delete quiz page.");

	    // Add data to the model for rendering the delete quiz page
	    mav.addObject("allCategorys", quizService.getAllCategories());
	    mav.addObject("allQuestions", quizService.getAllQuizs());
	    mav.addObject("allTypes", quizService.getAllTypes());
	    mav.addObject("allLevels", quizService.getAllDistinctLevels());
	    
	    // Set the view name for the delete quiz page
	    mav.setViewName("deleteQuiz");
	    return mav;
	}
}