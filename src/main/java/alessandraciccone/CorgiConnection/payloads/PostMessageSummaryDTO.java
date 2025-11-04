package alessandraciccone.CorgiConnection.payloads;

import java.util.UUID;

public record PostMessageSummaryDTO(
 UUID id,
 String content
) {
}
