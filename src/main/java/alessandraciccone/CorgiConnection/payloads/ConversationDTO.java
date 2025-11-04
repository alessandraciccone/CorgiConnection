package alessandraciccone.CorgiConnection.payloads;
import java.util.List;
import java.util.UUID;
public record ConversationDTO(
        UUID otherUser_Id,
String otherUsername,
        String otherUserProfileImage,
        List<MessageResponseDTO> messages,
        int unreadCount
) {
}
