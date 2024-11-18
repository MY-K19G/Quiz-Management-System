package k19g.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import k19g.quiz.entity.ApplicationSettings;

/**
 * Repository interface for accessing and managing {@link ApplicationSettings} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations and additional 
 * JPA functionalities for the {@link ApplicationSettings} entity.
 * </p>
 *
 * <p><strong>Author:</strong> K19G</p>
 */
@Repository
public interface ApplicationSettingsRepository  extends JpaRepository<ApplicationSettings, Integer> {
	
}
