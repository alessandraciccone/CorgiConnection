package alessandraciccone.CorgiConnection.entities;


import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private UUID id;

    @Column(nullable = false)
    private String questionText;


    public Question(){};

    public Question(String questionText, Quiz quiz, List<Answer> answers, Integer orderNumber) {

        this.questionText = questionText;
        this.quiz = quiz;
        this.answers = answers;
        this.orderNumber = orderNumber;
    }

    @ManyToOne
    @JoinColumn(name="quiz_id", nullable = false)
    private Quiz quiz;

    @OneToMany(mappedBy = "question",cascade=CascadeType.ALL)
    private List <Answer> answers;

    private Integer orderNumber;


    public UUID getId() {
        return id;
    }


    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", quiz=" + quiz +
                ", orderNumber=" + orderNumber +
                '}';
    }
}
