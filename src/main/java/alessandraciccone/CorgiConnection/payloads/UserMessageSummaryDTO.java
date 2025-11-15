package alessandraciccone.CorgiConnection.payloads;

import alessandraciccone.CorgiConnection.entities.User;

import java.util.UUID;

public record UserMessageSummaryDTO(
        UUID id,
        String username,
        String profileImage
) {
    // Costruttore personalizzato che accetta un oggetto User
    public UserMessageSummaryDTO(User user) {
        this(user.getId(), user.getUsername(), user.getProfileImage());
    }
}
