package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {

    Quiz findByTitleQuiz(String titleQuiz);
}
