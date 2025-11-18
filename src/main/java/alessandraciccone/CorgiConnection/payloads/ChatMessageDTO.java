package alessandraciccone.CorgiConnection.payloads;

public class ChatMessageDTO {
    private String sender;
    private String recipient;
    private String content;
    private Long timestamp;
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    // Constructors
    public ChatMessageDTO() {}

    public ChatMessageDTO(String sender, String content, MessageType type) {
        this.sender = sender;
        this.content = content;
        this.type = type;
    }

    // Getters e Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}