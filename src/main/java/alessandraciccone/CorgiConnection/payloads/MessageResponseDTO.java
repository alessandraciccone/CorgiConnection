package alessandraciccone.CorgiConnection.payloads;

import java.util.Date;
import java.util.UUID;

public record MessageResponseDTO(
        UUID id,
        String contentMessage,
        Date sentAt,
        boolean isRead,
        UserMessageSummaryDTO sender,
        UserMessageSummaryDTO receiver,
        PostMessageSummaryDTO relatedPost

) {
}
