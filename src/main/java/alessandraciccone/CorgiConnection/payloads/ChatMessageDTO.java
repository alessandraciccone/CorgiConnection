package alessandraciccone.CorgiConnection.payloads;

public record ChatMessageDTO(
        String sender,
        String recipient,
        String content,
        Long timestamp,
        MessageType type
) {
    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    public ChatMessageDTO(String sender, String content, MessageType type) {
        this(sender, null, content, null, type);
    }
}