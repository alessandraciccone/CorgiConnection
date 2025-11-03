package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Answer;
import alessandraciccone.CorgiConnection.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {

List<Answer> findByIsCorrect(boolean isCorrect);

List<Answer> findByQuestion(Question question);

List <Answer> findByQuestion_Id(UUID id);


@Query("SELECT a FROM Answer a WHERE a.question.id = :questionId AND a.isCorrect = true")
Optional<Answer> findCorrectAnswerByQuestionId(@Param("questionId") UUID questionId);

long countByQuestion_Id(UUID questionId);


@Query("SELECT COUNT(a) > 0 FROM Answer a WHERE a.question.id = :questionId AND a.isCorrect = true")
boolean hasCorrectAnswer(@Param("questionId") UUID questionId);}
