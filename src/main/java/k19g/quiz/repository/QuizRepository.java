package k19g.quiz.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import k19g.quiz.entity.Level;
import k19g.quiz.entity.Quiz;

/**
 * Repository interface for accessing and managing quiz data in the database.
 * 
 * <p>This interface extends JpaRepository and provides additional custom queries
 * for fetching quiz information such as categories, types, levels, and specific quiz details.
 * </p>
 * 
 * <p><b>Author:</b> K19G</p>
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {

	/**
	 * <p>Fetches a list of unique categories available in the quizzes.</p>
	 * 
	 * @return List of distinct categories.
	 */
	@Query("SELECT DISTINCT q.category FROM Quiz q")
	List<String> findDistinctCategoryBy();

	/**
     * <p>Fetches a list of unique quiz types available.</p>
     * 
     * @return List of distinct quiz types.
     */
	@Query("SELECT DISTINCT q.type FROM Quiz q")
	List<String> findDistinctTypeBy();

	/**
     * Retrieves all quiz question titles.
     * 
     * @return List of question titles.
     */
	@Query("SELECT q.questionTitle FROM Quiz q")
	List<String> findAllQuestionTitles();
	
	/**
     * Finds quizzes by level, category, and type with random ordering.
     * 
     * <p>This method supports pagination and optional filtering by category and type.</p>
     * 
     * @param pageable Pagination details.
     * @param level The level of the quiz.
     * @param category The category of the quiz (optional).
     * @param type The type of quiz (optional).
     * @return A paginated list of quizzes.
     */
	@Query("SELECT q FROM Quiz q " +
		       "WHERE (q.level = :level) " +
		       "AND (:category IS NULL OR :category = '' OR q.category = :category) " +
		       "AND (:type IS NULL OR :type = '' OR q.type = :type) " +
		       "ORDER BY RAND()")
		Page<Quiz> findByLevelCategoryAndType(Pageable pageable, 
		                                          @Param("level") Level level, 
		                                          @Param("category") String category, 
		                                          @Param("type") String type);

	/**
     * <p>Fetches the list of answers for a specific quiz question by ID.</p>
     * 
     * @param questionId The ID of the quiz question.
     * @return List of answers for the specified question.
     */
	@Query("SELECT q.answers FROM Quiz q WHERE q.id = :questionId")
    List<String> findAnswersByQuestionId(Integer questionId);
	
    /**
     * Retrieves random options for a quiz question by its ID.
     * 
     * <p>This query uses native SQL to retrieve options in random order for a given quiz question.</p>
     * 
     * @param quizId The ID of the quiz question.
     * @return List of options in random order.
     */

	@Query(value = "SELECT o.options FROM quiz_options o WHERE o.quiz_id = :quizId ORDER BY RAND()", nativeQuery = true)
	List<String> findRandomOptionsByQuizId(@Param("quizId") Integer quizId);

	/**
     * <p>Fetches a list of unique levels available in the quizzes.</p>
     * 
     * @return List of distinct quiz levels.
     */
	 @Query("SELECT DISTINCT q.level FROM Quiz q")
	 List<Level> findDistinctLevels();

}