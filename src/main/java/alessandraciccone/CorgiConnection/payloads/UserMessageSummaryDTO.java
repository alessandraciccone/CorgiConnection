package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record UserMessageSummaryDTO(
        UUID id,
        String username,
        String profileImage
) {
}
