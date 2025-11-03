package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record QuestionSummaryDTO(
        UUID id,
        String questionText
) {
}
