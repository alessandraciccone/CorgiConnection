package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record QuizUpdateDTO(

        @Size(min=2, max=100, message="Il titolo deve avere tra i 2 e i 100 caratteri")
        String titleQuiz,
        String descriptionQuiz,
        List<UUID> questionIds
) {
}
