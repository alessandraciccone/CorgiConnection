package alessandraciccone.CorgiConnection.payloads;

import java.util.List;
import java.util.UUID;

public record QuizResponseDTO(

        UUID id,
        String titleQuiz,
        String descriptionQuiz,
        List<UUID> questionIds
) {
}
