package k19g.quiz.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

/**
 * Entity class representing a quiz question in the application.
 * This class is mapped to the "quiz" table in the database and contains fields
 * related to the quiz question, including its type, category, title, options, and answers.
 * 
 * Author: K19G
 */
@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Unique identifier for the quiz question

    private String type; // Type of the quiz question (e.g., multiple choice, true/false)

    private String category; // Category of the quiz question
    
    @Lob
    private String questionTitle; // Title of the question
    
    @Lob
    private String questionCode; // Code for the question, if applicable
    
    @Lob
    private String explanation; // Explanation for the answer

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options = new ArrayList<>(4); // Options for the quiz question

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> answers = new ArrayList<>(4); // Correct answers' indices

    @Enumerated(EnumType.STRING)
    private Level level; // Difficulty level of the question

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Quiz [id=" + id + ", type=" + type + ", category=" + category + 
               ", questionTitle=" + questionTitle + ", questionCode=" + questionCode + 
               ", explanation=" + explanation + ", options=" + options + 
               ", answers=" + answers + ", level=" + level + "]";
    }
}

