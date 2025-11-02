package alessandraciccone.CorgiConnection.entities;

import jakarta.persistence.*;
import alessandraciccone.CorgiConnection.entities.User;

import java.util.UUID;

@Entity
@Table(name="quiz_result")
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
private Integer score;
private Integer totalQuestions;

    public QuizResult(){};

    public QuizResult( User user, Quiz quiz, Integer score, Integer totalQuestions) {
        this.user = user;
        this.quiz = quiz;
        this.score = score;
        this.totalQuestions = totalQuestions;
    }

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="quiz_id", nullable = false)
    private Quiz quiz;



    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    @Override
    public String toString() {
        return "QuizResult{" +
                "id=" + id +
                ", score=" + score +
                ", totalQuestions=" + totalQuestions +
                '}';
    }
}
