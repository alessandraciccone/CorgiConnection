package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Message;
import alessandraciccone.CorgiConnection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findBySentAt(Date sentAt);
    List <Message>  findByIsRead( boolean isRead);
    List <Message> findByReceiver (User receiver);
    List <Message> findByReceiver_Id (UUID receiver_Id);
    List<Message> findBySender (User sender);
    List <Message> findBySender_Id(UUID sender_Id);

    @Query
     ("SELECT m FROM Message m WHERE m.receiver.id = :receiverId AND m.isRead = false")
    List<Message> findUnreadMessagesByReceiver(@Param("receiverId") UUID receiverId);

    @Query
     ("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :receiverId AND m.isRead = false")
    long countUnreadMessagesByReceiver(@Param("receiverId") UUID receiverId);

    @Query
            ("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR " +
            "(m.sender.id = :user2Id AND m.receiver.id = :user1Id) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findConversationBetweenUsers(
            @Param("user1Id") UUID user1Id,
            @Param("user2Id") UUID user2I
    );
}
