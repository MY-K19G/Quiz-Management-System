package k19g.quiz.DTO;

import java.util.List;

import k19g.quiz.entity.Quiz;

/**
 * DTO for transferring quiz data. This class is used to represent the data
 * of a quiz that can be transferred over the network,
 * typically in response to API calls or for internal use.
 */
public class QuizDTO {
    
    private Integer id;               
    private String type;              
    private String category;          
    private String questionTitle;     
    private String questionCode;      
    private String explanation;       
    private List<String> options;     
    private List<String> answers;     
    private String level;             

    public QuizDTO() {};
    
    public QuizDTO(Integer id, String type, String category, String questionTitle, 
                   String questionCode, String explanation, List<String> options, 
                   List<String> answers, String level) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.questionTitle = questionTitle;
        this.questionCode = questionCode;
        this.explanation = explanation;
        this.options = options;
        this.answers = answers;
        this.level = level;
    }
    
    public static QuizDTO convertToDTO(Quiz quiz) {
        QuizDTO dto = new QuizDTO();
        dto.setId(quiz.getId());
        dto.setType(quiz.getType());
        dto.setCategory(quiz.getCategory());
        dto.setQuestionTitle(quiz.getQuestionTitle());
        dto.setQuestionCode(quiz.getQuestionCode());
        dto.setExplanation(quiz.getExplanation());
        dto.setOptions(quiz.getOptions());  
        dto.setAnswers(quiz.getAnswers());  
        dto.setLevel(quiz.getLevel().toString());
        return dto;
    }

    
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "QuizDTO [id=" + id + ", type=" + type + ", category=" + category + 
               ", questionTitle=" + questionTitle + ", questionCode=" + questionCode + 
               ", explanation=" + explanation + ", options=" + options + 
               ", answers=" + answers + ", level=" + level + "]";
    }
}
