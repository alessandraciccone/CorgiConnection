package alessandraciccone.CorgiConnection.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 1000)
    private String ContentMessage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt;

    private boolean isRead= false;


    public Message(){};

    public Message(String contentMessage, Date sentAt, boolean isRead, User receiver, User sender, Post relatedPost) {

        ContentMessage = contentMessage;
        this.sentAt = sentAt;
        this.isRead = isRead;
        this.receiver = receiver;
        this.sender=sender;
        this.relatedPost = relatedPost;
    }

    @ManyToOne
    @JoinColumn(name="receiver_id", nullable = false)
    private User receiver;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="post_id")
private Post relatedPost;

    public UUID getId() {
        return id;
    }

    public String getContentMessage() {
        return ContentMessage;
    }

    public void setContentMessage(String contentMessage) {
        ContentMessage = contentMessage;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender(){return sender;}

    public void setSender(User sender){this.sender=sender;}
    public Post getRelatedPost() {
        return relatedPost;
    }

    public void setRelatedPost(Post relatedPost) {
        this.relatedPost = relatedPost;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", ContentMessage='" + ContentMessage + '\'' +
                ", sentAt=" + sentAt +
                ", isRead=" + isRead +
                '}';
    }
}
