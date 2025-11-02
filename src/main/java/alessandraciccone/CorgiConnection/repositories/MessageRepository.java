package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findBySentAt(Date sentAt);
    List <Message>  findByIsRead( boolean isRead);

}
