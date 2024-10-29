package k19g.quiz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity class representing a user in the quiz application.
 * This class is mapped to the "quiz_user" table in the database and contains fields
 * related to the user's email, password, and role.
 * 
 * Author: K19G
 */
@Entity
@Table(name = "quiz_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId; // Unique identifier for the user

    @Column(unique = true, nullable = false, name = "user_email")
    private String userEmail; // User's email address, must be unique

    @Column(nullable = true, name = "user_password")
    private String userPassword; // User's password, stored securely

    @Column(nullable = true, name = "user_role")
    private String userRole; // Role of the user (e.g., admin, user)

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", userEmail=" + userEmail + 
               ", userPassword=" + userPassword + ", userRole=" + userRole + "]";
    }
}