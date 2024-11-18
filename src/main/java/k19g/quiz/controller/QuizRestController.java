package k19g.quiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import k19g.quiz.entity.Quiz;
import k19g.quiz.service.ApplicationSettingsService;
import k19g.quiz.service.QuizService;
import k19g.quiz.utils.MiscellaneousUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing quiz-related operations.
 *
 * <p>This controller provides endpoints to handle 
 * submitting and deleting quizzes.</p>
 *
 * <p>Each method is designed to handle its respective HTTP request, ensuring that 
 * the quiz data is managed according to the requested operation.</p>
 * 
 * <p><strong>Author:</strong> K19G</p>
 * 
 * @see Quiz
 * @see QuizService
 */
@RestController
@RequestMapping("/api")
public class QuizRestController {

    private static final Logger logger =LoggerFactory.getLogger(QuizRestController.class);;

    private final QuizService quizService;

	private final ApplicationSettingsService applicationSettingsService;
	
	private final String unsplashApiUrl;

	private final String accessKey;
	 
	private final String disable_text;
	 
	private final String enable_text;
	
    @Autowired
    private QuizRestController(
    		QuizService quizService, 
    		ApplicationSettingsService applicationSettingsService,
    		@Value("${UNSPLASH_API_URL}") String unsplashApiUrl,
    		@Value("${UNSPLASH_ACCESS_KEY}") String accessKey,
    		@Value("${DISABLE_TEXT}") String disable_text,
    		@Value("${ENABLE_TEXT}") String enable_text) {
    	
        this.quizService = quizService;
        this.applicationSettingsService = applicationSettingsService;
        this.unsplashApiUrl=unsplashApiUrl;
		this.accessKey = accessKey;
		this.disable_text = disable_text;
		this.enable_text = enable_text;
    }

    /**
    * REST Controller method to delete a quiz question by its ID.
    * 
    * <p>This method handles the HTTP DELETE request to remove a quiz question from the database. 
    * If the quiz question is successfully deleted, it returns a success message. If the question 
    * is not found, it returns a 404 Not Found status.</p>
    *
    * @param id the ID of the quiz question to delete
    * @return ResponseEntity containing the result of the deletion, either a success or not found message
    */
   @DeleteMapping("/delete-quiz/{id}")
   public ResponseEntity<String> deleteQuestion(@PathVariable Integer id) {
       
	   logger.info("Received request to delete quiz with ID: {}", id);
       
       boolean isDeleted = quizService.deleteQuizById(id);
       
       logger.debug("Deletion status for quiz ID {}: {}", id, isDeleted);

       if (isDeleted) {
           logger.info("Successfully deleted quiz with ID: {}", id);
           return ResponseEntity.ok("Question deleted successfully");
       } else {
           logger.warn("Quiz with ID {} not found, deletion failed", id);
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found");
       }
   }
    
   
   
   /**
    * <p>Handles the submission of quiz results. Processes the JSON input, stores the quiz answers 
    * in the session, and tracks the time taken for quiz completion.</p>
    *
    * @param requestBody The JSON array containing quiz data.
    * @param session The HTTP session used to store quiz-related data.
    * @return A ResponseEntity indicating the success of the operation.
    */
   
   @PostMapping("/submit_quiz")
   public ResponseEntity<Void> submitQuiz(@RequestBody Map<String, Object> requestBody,HttpSession session) {
       try {
           logger.info("Processing quiz submission");

    	   Long submissionTime =((Number) requestBody.get("submissionTime")).longValue();
    	   
           logger.debug("Received requestBody from quiz page: {}", requestBody);

    	   List<Map<String, List<Integer>>> answerArray = (List<Map<String, List<Integer>>>) requestBody.get("AnswerArray");

    	   List<List<Integer>> answerArrayList = new ArrayList<>();

    	   if (answerArray != null) { 
    	       for (Map<String, List<Integer>> map : answerArray) {
    	          
    	    	   List<Integer> selectedAnswer = map.get("selectedOptions");
    	           
    	    	   if (selectedAnswer == null) {
    	               selectedAnswer = new ArrayList<>();
    	           }

    	           answerArrayList.add(selectedAnswer);
    	       }
    	   }

           List<Map<String, Object>> answers = (List<Map<String, Object>>) requestBody.get("answers");
           
           Map<Integer, List<String>> answerMap = new HashMap<>();
          
           for (Map<String, Object> answerDetail : answers) {
           
        	   Integer questionId = (Integer) answerDetail.get("questionId");
           
        	   List<String> selectedOptions = (List<String>) answerDetail.get("selectedOptions");
               
        	   answerMap.put(questionId, selectedOptions);
           
           }

           session.setAttribute("submissionTime", MiscellaneousUtils.convertSecondsToTime(submissionTime));
           session.setAttribute("attemptQuizAnswer", answerArrayList);
           session.setAttribute("attemptQuiz", answerMap);
           
           logger.info("Quiz submission processed and stored in session");
           
          return  ResponseEntity.ok().build();
       } 
       catch (Exception e) {
	           logger.error("Error processing quiz submission", e);
	           e.printStackTrace();
	          return  ResponseEntity.badRequest().build();
       }
   }
   
   /**
    * Helper method to process the attempted answers as integer arrays.
    * 
    * @param mapObj The map containing the attempted answers.
    * @param session The HTTP session to store quiz data.
    */
    public void returnIntegerArrayAttemptedAns(Map<Integer, List<String>> mapObj,HttpSession session) {
    	
    	List<Quiz> generatedQuestions=(List<Quiz>)session.getAttribute("Quiz_questions");
    	
    	for(Map.Entry<Integer, List<String>> map:mapObj.entrySet()) {
//    		map.getValue();
    		for(int i=0;i<generatedQuestions.size();i++) {
    			generatedQuestions.get(i).getOptions();
    		}
    	}
        logger.info("Attempted answers processed successfully");
    }

    /**
     * Uploads a JSON file containing quiz data and stores it in the database.
     *
     * @param file the JSON file containing quiz data
     * @return ResponseEntity with a success message or an error message
     */
    @PostMapping("/uploadJson")
    public ResponseEntity<String> uploadQuizData(@RequestParam("jsonfile") MultipartFile file) {
        
    	logger.info("Received request to upload quiz data from file: {}", file.getOriginalFilename());

        try {
            quizService.saveJSONQuiz(file); 
            logger.info("Successfully uploaded quiz data from file: {}", file.getOriginalFilename());
            return ResponseEntity.ok("Quiz data uploaded and stored successfully.");
       
        } catch (IOException e) {
            logger.error("Failed to upload quiz data from file: {}. Error: {}", file.getOriginalFilename(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to upload quiz data: ");
        }
    }
    
    /**
     * Validates the input and disable the /register endpoint from controller if valid.
     *
     * @param input the input to validate
     * @return response message indicating the result of the validation
     */
    @PostMapping("/validateRegister")
    public ResponseEntity<String> validateRegister(@RequestBody Map<String, String> request,HttpSession httpSession ) {
    	
    	if(!(boolean)httpSession.getAttribute("fromValidateRegister") || httpSession.getAttribute("fromValidateRegister")==null) {
    		httpSession.setAttribute("fromValidateRegister", false);
    		return ResponseEntity.ok("You didn't come from /validateRegister.");
    	}
    		
    	
    	String secretText = request.get("secret_text");
    	 
        logger.info("Received input for validation: {}", secretText);
        
        if (secretText.equals(disable_text)) {
            if (!applicationSettingsService.getToggleRegister()) {
                logger.info("Access to /register has already been disabled.");
                return ResponseEntity.ok("Access to /register has already been disabled.");
            }

            applicationSettingsService.setToggleRegister(false);
            logger.info("Remove validation: /register endpoint has been disabled from controller.");
            return ResponseEntity.ok("Access to /register has been disabled.");
        } 

        if (secretText.equals(enable_text)) {
            if (applicationSettingsService.getToggleRegister()) {
                logger.info("Access to /register has already been enabled.");
                return ResponseEntity.ok("Access to /register has already been enabled.");
            }

            applicationSettingsService.setToggleRegister(true);
            logger.info("Add validation: /register endpoint has been enabled from controller.");
            return ResponseEntity.ok("Access to /register has been enabled.");
        }

        logger.info("Wrong validation: /register endpoint cannot be modified due to incorrect validation text.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body("Invalid validation text. Access denied.");

        
    }
    
    /**
    * Handles HTTP GET requests to fetch a random background image URL from the Unsplash API.
    *
    * <p>This method constructs the request URL using the Unsplash API base URL and the access key. 
    * It sends a GET request to the Unsplash API and expects to receive a response containing the URL 
    * of a random image. The extracted image URL is then returned as a JSON object in the response.</p>
    *
    * @return ResponseEntity containing a map with the key "imageUrl" and the corresponding 
    *         URL of the fetched background image.
    *
    * @throws RestClientException if an error occurs while making the REST call to the Unsplash API.
    *
    */
    @GetMapping("/get-background-image")
    public ResponseEntity<Map<String, String>> getBackgroundImage() {
        
    	String fullUrl = unsplashApiUrl + "&client_id=" + accessKey;
        
    	RestTemplate restTemplate = new RestTemplate();
        
    	Map<String, Object> response = restTemplate.getForObject(fullUrl, Map.class);

        Map<String, String> result = new HashMap<>();
        
        result.put("imageUrl", (String) ((Map) response.get("urls")).get("full"));
        
        return ResponseEntity.ok(result);
    }

}

