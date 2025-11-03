package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record AuthorSummaryDTO(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String city,
        String profileImage

) {
}
