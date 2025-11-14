package alessandraciccone.CorgiConnection.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PostResponseDTO(
        UUID id,
        String content,
<<<<<<< Updated upstream
        UUID corgi_Id,
        LocalDate date,
        AuthorSummaryDTO author,
        CorgiSummaryDTO corgi,
        List<PhotoSummaryDTO> photos,
=======
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,
        AuthorSummaryDTO author,
>>>>>>> Stashed changes
        int commentsCount
) {}

