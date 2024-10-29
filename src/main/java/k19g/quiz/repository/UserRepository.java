package k19g.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import k19g.quiz.entity.User;

/**
 * Repository interface for accessing and managing user data in the database.
 * 
 * <p>This interface extends JpaRepository to provide CRUD operations and a custom query
 * to find a user by email.</p>
 * 
 * <p><b>Author:</b> K19G</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	  /**
     * Finds a user by their email address.
     * 
     * @param email The email address of the user.
     * @return The user with the specified email, or null if no user is found.
     */
	User findByuserEmail(String email);
}
