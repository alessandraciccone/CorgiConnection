package alessandraciccone.CorgiConnection.payloads;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PostResponseDTO(
        UUID id,
        String content,
        LocalDate date,
        AuthorSummaryDTO author,

        List<PhotoSummaryDTO> photos,
        int commentsCount

) {
}
