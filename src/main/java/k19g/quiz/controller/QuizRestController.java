package k19g.quiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import k19g.quiz.entity.Quiz;
import k19g.quiz.service.QuizService;
import k19g.quiz.utils.MiscellaneousUtils;

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
 * <h3>Available Endpoints:</h3>
 * <ul>
 *   <li><b>POST /api/submit_quiz</b> - Submit quiz from users.</li>
 *   <li><b>DELETE /api/delete-quiz/{id}</b> - Deletes a quiz by its ID.</li>
 * </ul>
 *
 * <p>Each method is designed to handle its respective HTTP request, ensuring that 
 * the quiz data is managed according to the requested operation.</p>
 *
 * @see Quiz
 * @see QuizService
 */
@RestController
@RequestMapping("/api")
public class QuizRestController {

    private static final Logger logger = LoggerFactory.getLogger(QuizRestController.class);

    private final QuizService quizService;

    @Autowired
    public QuizRestController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
    * Controller method to delete a quiz question by its ID.
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
       
       // Attempt to delete the quiz by its ID
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

           // Extract submissionTime
    	   Long submissionTime =((Number) requestBody.get("submissionTime")).longValue();
    	   
           logger.debug("Received requestBody from quiz page: {}", requestBody);

    	// Retrieve AnswerArray from the request body
    	   List<Map<String, List<Integer>>> answerArray = (List<Map<String, List<Integer>>>) requestBody.get("AnswerArray");

    	   // Initialize the resulting List<List<Integer>>
    	   List<List<Integer>> answerArrayList = new ArrayList<>();

    	   // Iterate over the answerArray
    	   if (answerArray != null) {  // Ensure answerArray itself is not null
    	       for (Map<String, List<Integer>> map : answerArray) {
    	           List<Integer> selectedAnswer = map.get("selectedOptions");
    	           // If selectedAnswer is null, replace it with an empty list
    	           if (selectedAnswer == null) {
    	               selectedAnswer = new ArrayList<>();
    	           }

    	           // Add the selectedAnswer (either the original or an empty list) to answerArrayList
    	           answerArrayList.add(selectedAnswer);
    	       }
    	   }



           // Extract answers and construct the answer map
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
    		map.getValue();
    		for(int i=0;i<generatedQuestions.size();i++) {
    			generatedQuestions.get(i).getOptions();
    		}
    	}
        logger.info("Attempted answers processed successfully");
    }


}
