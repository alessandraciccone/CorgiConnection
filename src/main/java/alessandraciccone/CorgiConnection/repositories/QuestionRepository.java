package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    List<Question> findByQuiz_Id(UUID quiz_Id);
}
