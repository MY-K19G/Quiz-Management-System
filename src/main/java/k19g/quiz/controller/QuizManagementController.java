package k19g.quiz.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;
import k19g.quiz.entity.Quiz;
import k19g.quiz.exception.QuizAnswerNotFoundException;
import k19g.quiz.exception.QuizIdInvalidException;
import k19g.quiz.exception.QuizNotFoundException;
import k19g.quiz.service.QuizService;
import k19g.quiz.utils.MiscellaneousUtils;

/**
 * <h1>Quiz Management Controller</h1>
 * <p>This controller manages the main operations related to quizzes in the application,
 * including creation, updating, editing, and deletion. Each method provides functionality
 * to handle HTTP requests for different quiz management operations.</p>
 * 
 * <p><strong>Author:</strong> K19G</p>
 */
@Controller
public class QuizManagementController {
	
    private static final Logger logger = LoggerFactory.getLogger(QuizManagementController.class);
	
    private final QuizService quizService;

	 @Autowired
     public CacheManager cacheManager;
    
	@Autowired
	private QuizManagementController(QuizService quizService){
		this.quizService=quizService;
	}

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
        return "CreateQuiz"; 
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
		ModelAndView mav= new ModelAndView();
		
	    if (theoryQ == null || theoryQ.trim().isEmpty()) {
	        quiz.setQuestionCode(progQ);
	    } else {
	        quiz.setQuestionTitle(theoryQ);
	    }

	    quiz.setType(type);
	    
	    List<String> answerList = quiz.getAnswers().stream()
	    	    .map(answerID -> quiz.getOptions().get(Integer.parseInt(answerID)))
	    	    .filter(Objects::nonNull)
	    	    .filter(ans -> !ans.trim().isEmpty())
	    	    .collect(Collectors.toList());

	    quiz.setAnswers(answerList);

	    logger.debug("Prepared quiz object: {}", quiz);
	    
	    boolean isInserted = quizService.saveQuiz(quiz)!=null; 
	    session.setAttribute("isQuizInserted", isInserted);
        logger.info("Quiz saved status: {}", isInserted);

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

		logger.info("Entering quizUpdatePage method. Session ID: {}", session.getId());
	    
	    ModelAndView mav = new ModelAndView();
	    
	    mav.addObject("allCategories", quizService.getAllCategories());
	    mav.addObject("allTypes", quizService.getAllTypes());
	    mav.addObject("allLevels", quizService.getAllDistinctLevels());
	    mav.addObject("QuestionEntity", quizService.getAllQuizzes());

	    session.setAttribute("fromUpdatePage", true);

	    logger.info("Quiz update page data prepared.");
	    
	    mav.setViewName("updateQuiz");

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

        logger.info("Received request to edit quiz. Question ID: {}", questionId);
	    
	    Boolean fromUpdatePage = (Boolean) session.getAttribute("fromUpdatePage");
	    
	    if (questionId == null) {
	    	throw new QuizIdInvalidException("No Quiz ID provided.\n or \nCheck the url edit?questionId="+questionId);
	    } else {
	    	
	        Quiz optionalQuiz = quizService.getQuiz(questionId).get();
	    	
	        MiscellaneousUtils.checkIfEmpty(optionalQuiz,
	        		new QuizNotFoundException("No quiz found for Question ID: "+questionId));

	        List<String> answerList=quizService.getAnswerById(questionId);
	        MiscellaneousUtils.checkIfListIsEmpty(answerList, 
	        		new QuizAnswerNotFoundException("No answer(String) found for Question ID: "+questionId));
	       
	        
	        List<Integer> answerIntList=convertIntAnswerList(answerList,optionalQuiz.getOptions());
	        MiscellaneousUtils.checkIfListIsEmpty(answerIntList, 
	        		new QuizAnswerNotFoundException("No answer(Integer Index) found for Question ID: "+questionId));
	        	
	            mav.addObject("QuestionEntity", optionalQuiz);
	            mav.addObject("answers", answerIntList);
	            mav.setViewName("editQuiz");
	    }
	    
	    if (fromUpdatePage == null || !fromUpdatePage) {
	    	mav.addObject("homepage_url", "/update");
	        mav.setViewName("403"); 
            logger.warn("Access denied. User tried to edit quiz without coming from update page.");
            return mav;
	    }

	    return mav;
	}
	
	
	/**
	 * Converts a list of answer strings to their corresponding indices based on a list of options.
	 * Each answer in the answer list is matched against the options, and the index of the matching
	 * option is stored in an integer list.
	 * <p>
	 * For example, if the answer list is ["B", "D"] and the options are ["A", "B", "C", "D"], 
	 * the output will be [1, 3].
	 * </p>
	 *
	 * @param answerList a list of answer strings to be converted to indices
	 * @param options a list of possible options where each answer in {@code answerList} is searched
	 * @return a list of integers representing the indices of each answer in the {@code answerList}
	 *         within the {@code options} list
	 * @throws NullPointerException if either {@code answerList} or {@code options} is null
	 *
	 */
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



	/**
	 * Handles the HTTP POST request for updating/editing a quiz. 
	 * Updates the quiz with the provided theory or programming question based on the type specified.
	 *
	 * @param quiz the {@link Quiz} object containing the quiz details to be updated
	 * @param theoryQ a string representing the theory question for the quiz
	 * @param progQ a string representing the programming question for the quiz
	 * @param type the type of the question, either "Theory" or "Programming"; defaults to "Theory" if not provided
	 * @return a {@link ModelAndView} object that redirects to the "/update" view after the quiz is updated
	 */
	@PostMapping("/update_quiz") 
	public ModelAndView updateQuiz(@ModelAttribute Quiz quiz, 
			@RequestParam("theory_question") String theoryQ,
			@RequestParam("programming_question") String progQ,
			@RequestParam(value = "type", defaultValue = "Theory") String type) {

        logger.info("Updating quiz with TheoryQ: {}, ProgQ: {}, Type: {}", theoryQ, progQ, type);
        	ModelAndView mav = new ModelAndView();
        
			if("Theory".equalsIgnoreCase(type)) {
				
				quiz.setQuestionTitle(theoryQ);
				quiz.setType(type);
				
			}else {
				quiz.setQuestionCode(progQ);
			}
			quizService.saveQuiz(quiz);
			
	        logger.info("Quiz updated: {}", quiz);
	        
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
		
		ModelAndView mav= new ModelAndView();

		logger.info("Accessing delete quiz page.");

	    mav.addObject("allCategorys", quizService.getAllCategories());
	    mav.addObject("allQuestions", quizService.getAllQuizzes());
	    mav.addObject("allTypes", quizService.getAllTypes());
	    mav.addObject("allLevels", quizService.getAllDistinctLevels());
	    
	    mav.setViewName("deleteQuiz");
	    
	    return mav;
	}
	
	/**
     * Displays the JSON upload page for quiz data.
     *
     * @return the name of the view to render
     */
	@GetMapping("/upload")
	public String showJSON_UploadPage() {
        
		logger.info("Accessed the JSON upload page.");
		
        return "uploadQuizJSON";
	}
	
	/**
     * Displays the validate Register page for quiz data.
     *
     * @return the name of the view to render
     */
	@GetMapping("/validateRegister")
	public String showValidateRegister(HttpSession httpSession) {
		
		httpSession.setAttribute("fromValidateRegister", true);
        
		logger.info("Accessed the validate Register page.");
		
		return "validateRegister";
	}
	
	/**
     * Handles the GET request to display the export page with all available categories,
     * types, and levels for filtering quiz questions.
     * 
     * @return A ModelAndView object representing the "export" page with populated data for categories,
     *         types, and levels.
     */
	@GetMapping("/export")
	public ModelAndView exportPage() {
		logger.info("Fetching data for the export page.");
		ModelAndView mav= new ModelAndView("export");

		try {
	          mav.addObject("allCategorys", quizService.getAllCategories());
	          mav.addObject("allTypes", quizService.getAllTypes());
	          mav.addObject("allLevels", quizService.getAllDistinctLevels());

             logger.info("Successfully populated categories, types, and levels for the export page.");
	        } catch (Exception e) {
	            logger.error("Error occurred while fetching data for the export page.", e);
	        }   	
		return mav;
	}
	
	/**
     * Filters quiz based on the provided criteria and returns a downloadable JSON file
     * containing the filtered questions.
     * 
     * @param filters A map containing filter parameters: "type", "level", and "category".
     * @return ResponseEntity containing the filtered questions in JSON format as a downloadable file.
     */
	@PostMapping("/filterQuestions")
    public ResponseEntity<?> filterQuestions(@RequestBody Map<String, String> filters) {
		String type = filters.get("type");
	    String level = filters.get("level");
	    String category = filters.get("category");
	    
	    String filename = Stream.of(level, type, category)
                .filter(part -> part != null && !part.isEmpty())
                .collect(Collectors.joining("-"));

	    if (!filename.isEmpty()) {
	        filename += "-quiz";
	    } else {
	        filename = "quiz";
	    }
	    
        System.err.println(filename);
	    logger.info("Received filters - Type: {}, Level: {}, Category: {}", type, level, category);
	    
	    try {
	    	
			List<Quiz> filteredQuestions = quizService.getFilteredQuestions(type, level, category);
			
		    if (filteredQuestions.isEmpty()) {
	            logger.warn("No Quiz found for filters - Type: {}, Level: {}, Category: {}", type, level, category);
		        
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                .body("No Quiz found for the provided filters.");
		    }

	    	 ObjectMapper objectMapper = new ObjectMapper();
	         String jsonContent = objectMapper.writeValueAsString(filteredQuestions);

	         byte[] contentBytes = jsonContent.getBytes();

	         ByteArrayResource resource = new ByteArrayResource(contentBytes);

	         HttpHeaders headers = new HttpHeaders();
	         headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename+".json");
	         headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

	         logger.info("Filtered questions generated successfully. Returning as downloadable file.");
	         
	         return ResponseEntity.ok()
	                 .headers(headers)
	                 .contentLength(contentBytes.length)
	                 .body(resource);

	    } catch (Exception e) {
            logger.error("Error while generating JSON file for filtered questions.", e);
	        
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(null);
	    }
	}
	
	
}