package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record PhotoSummaryDTO(
        UUID id,
        String url,
        String caption
) {
}
