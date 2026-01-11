package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record QuizWithQuestionsDTO(
        @NotBlank(message="Il titolo del quiz è obbligatorio!")
        @Size(min=2, max=100, message="Il titolo del quiz deve avere tra i 2 e i 100 caratteri")
        String titleQuiz,

        String descriptionQuiz,

        List<QuestionDTO> questions
) {}
// questo record serve a trasportare i dati di un quiz insieme alle sue domande associate in una singola struttura dati.