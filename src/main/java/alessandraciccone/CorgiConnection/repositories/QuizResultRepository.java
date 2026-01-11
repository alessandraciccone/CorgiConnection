package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizResultRepository extends JpaRepository<QuizResult, UUID> {
}
