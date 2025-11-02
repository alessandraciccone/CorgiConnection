package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {

List<Answer> findByIsCorrect(boolean isCorrect);
}
