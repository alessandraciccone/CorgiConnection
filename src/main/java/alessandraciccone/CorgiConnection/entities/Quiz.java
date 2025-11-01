package alessandraciccone.CorgiConnection.entities;


import jakarta.persistence.*;
import java.util.List;

import java.util.UUID;

@Entity
@Table(name="quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)

    private UUID id;

    @Column(nullable = false)
    private String titleQuiz;
    private  String descriptionQuiz;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> questions;

    public Quiz(){};

    public Quiz( String titleQuiz, String descriptionQuiz, List<Question> questions) {

        this.titleQuiz = titleQuiz;
        this.descriptionQuiz = descriptionQuiz;
        this.questions = questions;
    }

    public UUID getId() {
        return id;
    }

    public String getTitleQuiz() {
        return titleQuiz;
    }

    public void setTitleQuiz(String titleQuiz) {
        this.titleQuiz = titleQuiz;
    }

    public String getDescriptionQuiz() {
        return descriptionQuiz;
    }

    public void setDescriptionQuiz(String descriptionQuiz) {
        this.descriptionQuiz = descriptionQuiz;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", titleQuiz='" + titleQuiz + '\'' +
                ", descriptionQuiz='" + descriptionQuiz + '\'' +
                '}';
    }
}
