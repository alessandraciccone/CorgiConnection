package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.payloads.ChatMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Messaggio pubblico a tutti (chat di gruppo)
     */
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessageDTO sendPublicMessage(ChatMessageDTO message, @AuthenticationPrincipal User user) {
        // Creiamo un nuovo record con i dati corretti
        return new ChatMessageDTO(
                user.getUsername(),           // sender
                message.recipient(),          // recipient (può essere null)
                message.content(),            // content
                System.currentTimeMillis(),   // timestamp
                message.type()                // type
        );
    }

    /**
     * Messaggio privato a un utente specifico
     */
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(ChatMessageDTO message, @AuthenticationPrincipal User user) {
        ChatMessageDTO privateMessage = new ChatMessageDTO(
                user.getUsername(),           // sender
                message.recipient(),          // recipient
                message.content(),            // content
                System.currentTimeMillis(),   // timestamp
                message.type()                // type
        );

        // Invio al destinatario specifico
        messagingTemplate.convertAndSendToUser(
                message.recipient(),          // username destinatario
                "/queue/messages",            // destinazione WebSocket privata
                privateMessage
        );
    }
}
