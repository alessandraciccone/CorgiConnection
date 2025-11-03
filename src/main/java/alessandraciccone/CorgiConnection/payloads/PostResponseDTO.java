package alessandraciccone.CorgiConnection.payloads;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PostResponseDTO(
        UUID id,
        String content,
        UUID corgi_Id,
        LocalDate date,
        AuthorSummaryDTO author,
        CorgiSummaryDTO corgi,
        List<PhotoSummaryDTO> photos,
        int commentsCount

) {
}
