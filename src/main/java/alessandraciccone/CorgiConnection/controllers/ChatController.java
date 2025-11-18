package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.payloads.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Messaggio pubblico a tutti (chat di gruppo)
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessageDTO sendPublicMessage(
            ChatMessageDTO message,
            @AuthenticationPrincipal User user) {

        message.setSender(user.getUsername());
        message.setTimestamp(System.currentTimeMillis());
        return message;
    }

    // Messaggio privato a un utente specifico
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(
            ChatMessageDTO message,
            @AuthenticationPrincipal User user) {

        message.setSender(user.getUsername());
        message.setTimestamp(System.currentTimeMillis());

        // Invia al destinatario
        messagingTemplate.convertAndSendToUser(
                message.getRecipient(),
                "/queue/messages",
                message
        );
    }
}