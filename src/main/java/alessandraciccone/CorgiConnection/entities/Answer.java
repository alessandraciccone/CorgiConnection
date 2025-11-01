package alessandraciccone.CorgiConnection.entities;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="answers")
public class Answer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)

    private UUID id;


    @Column(nullable = false)

    private String answerText;
    private Boolean isCorrect=true;


    public Answer(){};

    public Answer( String answerText, Boolean isCorrect, Question question) {

        this.answerText = answerText;
        this.isCorrect = isCorrect;
        this.question = question;
    }

    @ManyToOne
    @JoinColumn(name="question_id", nullable = false)
    private Question question;

    public UUID getId() {
        return id;
    }



    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", answerText='" + answerText + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
