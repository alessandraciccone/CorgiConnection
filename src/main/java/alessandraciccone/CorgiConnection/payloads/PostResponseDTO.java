package alessandraciccone.CorgiConnection.payloads;

import java.time.LocalDate;
import java.util.UUID;

public record PostResponseDTO(
        UUID id,
        String content,
        LocalDate date,
        AuthorSummaryDTO author,

        int commentsCount

) {
}
