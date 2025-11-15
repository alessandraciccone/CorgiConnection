package alessandraciccone.CorgiConnection.controllers;

import alessandraciccone.CorgiConnection.payloads.ConversationDTO;
import alessandraciccone.CorgiConnection.payloads.MessageDTO;
import alessandraciccone.CorgiConnection.payloads.MessageResponseDTO;
import alessandraciccone.CorgiConnection.payloads.MessageUpdateDTO;
import alessandraciccone.CorgiConnection.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    public MessageService messageService;


//AGGIUNGI UN MESSAGGIO
    //http://localhost:3001/messages

    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(@RequestBody MessageDTO dto) {
        return ResponseEntity.ok(messageService.sendMessage(dto));
    }

    //TROVA MESSAGGIO PER ID
    //http://locahost:3001/messages/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> getMessageById(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }


//GGIORNO MESSAGGIO//http://localhost:3001/messages/{id}

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> updateMessage(
            @PathVariable UUID id,
            @PathVariable MessageUpdateDTO updateDTO
    ) {
        return ResponseEntity.ok(messageService.updateMessage(id, updateDTO));
    }

//ELIMINO MESSAGGIO
    //http://localhost:3001/messages/{id}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }


    //SEGNO MESSAAGGIO COME LETTO
//http://localhost:3001/messages/{id}/read
    @PutMapping("/{id}/read")
    public ResponseEntity<MessageResponseDTO> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.markAsRead(id));
    }

//SEGNA TUTTO I MESSAGGI LETTI X UN UTENTE
    //http://localhost:3001/messages/real-all/{userId}

    @PatchMapping("/read-all{userId}")
    public ResponseEntity<Void> markAllAsRead(@PathVariable UUID userId) {
        messageService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    //trova messaggi ricevuto

    //http://localhost:3001/messages/received{userID}
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<MessageResponseDTO>> getReceivedMessage(@PathVariable UUID userId) {
        return ResponseEntity.ok(messageService.getReceivedMessages(userId));
    }

    //TROVO MESSAGGI INVIATI
//http://localhost:3001/messages/sent{userID}
    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<MessageResponseDTO>> getSentMessage(@PathVariable UUID userId) {
        return ResponseEntity.ok(messageService.getSentMessages(userId));
    }

    //TROVO MESSAGGI NON LETTI
    //http://localhost:3001/messages/unread{userID}
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<MessageResponseDTO>> getUnreadMessage(@PathVariable UUID userId) {
        return ResponseEntity.ok(messageService.getUnreadMessages(userId));
    }

    //CONTO MESSAGGI NON LETTI
    //http://localhost:3001/messages/unread/count/{userId}
    @GetMapping("/unread/count/{unread}")
    public ResponseEntity<Long> countUnreadMessages(@PathVariable UUID userId) {
        return ResponseEntity.ok(messageService.countUnreadMessages(userId));
    }


    // CONVERSAZIONE TRA DUE UTENTI
// http://localhost:3001/messages/conversation/{user1Id}/{user2Id}
    @GetMapping("/conversation/{user1Id}/{user2Id}")
    public ResponseEntity<List<MessageResponseDTO>> getConversation(
            @PathVariable UUID user1Id,
            @PathVariable UUID user2Id
    ) {
        return ResponseEntity.ok(messageService.getConversation(user1Id, user2Id));
    }

    // TUTTE LE CONVERSAZIONI DI UN UTENTE
// http://localhost:3001/messages/conversation/{userId}
    @GetMapping("/conversation/{userId}")
    public ResponseEntity<List<ConversationDTO>> getAllConversations(@PathVariable UUID userId) {
        return ResponseEntity.ok(messageService.getAllConversations(userId));
    }
}