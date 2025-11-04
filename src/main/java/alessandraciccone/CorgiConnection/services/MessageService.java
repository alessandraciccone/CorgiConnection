package alessandraciccone.CorgiConnection.services;

import alessandraciccone.CorgiConnection.entities.Message;
import alessandraciccone.CorgiConnection.entities.Post;
import alessandraciccone.CorgiConnection.entities.User;
import alessandraciccone.CorgiConnection.exceptions.BadRequestException;
import alessandraciccone.CorgiConnection.exceptions.NotFoundException;
import alessandraciccone.CorgiConnection.payloads.*;
import alessandraciccone.CorgiConnection.repositories.MessageRepository;
import alessandraciccone.CorgiConnection.repositories.PostRepository;
import alessandraciccone.CorgiConnection.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    //invia nuovo messaggio
    public MessageResponseDTO sendMessage(MessageDTO messageDTO) {
        // verifico che mittente e destinatario esistano
        User sender = userRepository.findById(messageDTO.sender_Id())
                .orElseThrow(() -> new NotFoundException(
                        "Mittente con id " + messageDTO.sender_Id() + " non trovato"));

        User receiver = userRepository.findById(messageDTO.receiver_Id())
                .orElseThrow(() -> new NotFoundException(
                        "Destinatario con id " + messageDTO.receiver_Id() + " non trovato"));
//verifico cbe mittente  e destinatario non siano la stessa persona
        if (sender.getId().equals(receiver.getId())) {
            throw new BadRequestException("Non puoi inviare messaggi a te stesso");
        }
//verifico post
        Post relatedPost = null;
        if (messageDTO.relatedPost_Id() != null) {
            relatedPost = postRepository.findById(messageDTO.relatedPost_Id())
                    .orElseThrow(() -> new NotFoundException("Post con id " + messageDTO.relatedPost_Id() + " non trovato"));
        }

        Message newMessage = new Message();
        newMessage.setContentMessage(messageDTO.contentMessage());
        newMessage.setSentAt(new Date());
        newMessage.setRead(false);
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setRelatedPost(relatedPost);

        Message savedMessage = messageRepository.save(newMessage);
        return mapToResponseDTO(savedMessage);
    }

//trovo messaggio per id

    public MessageResponseDTO getMessageById(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Messaggio con id " + id + " non trovato"));
        return mapToResponseDTO(message);
    }

    //aggiorno messaggio
    public MessageResponseDTO updateMessage(UUID id, MessageUpdateDTO updateDTO) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Messaggio con id " + id + " non trovato"));

        if (updateDTO.contentMessage() != null) {
            message.setContentMessage(updateDTO.contentMessage());
        }
        if (updateDTO.isRead() != null) {
            message.setRead(updateDTO.isRead());
        }

        Message updatedMessage = messageRepository.save(message);
        return mapToResponseDTO(updatedMessage);
    }

    //elimino messaggio
    public void deleteMessage(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Messaggio con id " + id + " non trovato"));
        messageRepository.delete(message);
    }

    //segno messaggio come letto
    public MessageResponseDTO markAsRead(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException(
                        "Messaggio con id " + messageId + " non trovato"));

        message.setRead(true);
        Message updated = messageRepository.save(message);
        return mapToResponseDTO(updated);
    }

//segno tutti i messaggi come letti

    public void markAllAsRead(UUID userId) {
        List<Message> unreadMessages = messageRepository.findAll().stream()
                .filter(msg -> msg.getReceiver().getId().equals(userId) && !msg.isRead())
                .collect(Collectors.toList());

        unreadMessages.forEach(msg -> msg.setRead(true));
        messageRepository.saveAll(unreadMessages);
    }

    //trovo i messaggi di un utente
    public List<MessageResponseDTO> getReceivedMessages(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Utente con id " + userId + " non trovato"));

        return messageRepository.findAll().stream()
                .filter(msg -> msg.getReceiver().getId().equals(userId))
                .sorted(Comparator.comparing(Message::getSentAt).reversed())
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //trovo messaggi inviati da un utente
    public List<MessageResponseDTO> getSentMessages(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Utente con id " + userId + " non trovato"));

        return messageRepository.findAll().stream()
                .filter(msg -> msg.getSender().getId().equals(userId))
                .sorted(Comparator.comparing(Message::getSentAt).reversed())
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //trovo messaggi non letti
    public List<MessageResponseDTO> getUnreadMessages(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Utente con id " + userId + " non trovato"));

        return messageRepository.findByIsRead(false).stream()
                .filter(msg -> msg.getReceiver().getId().equals(userId))
                .sorted(Comparator.comparing(Message::getSentAt).reversed())
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //conto i messaggi non letti
    public long countUnreadMessages(UUID userId) {
        return messageRepository.findByIsRead(false).stream()
                .filter(msg -> msg.getReceiver().getId().equals(userId))
                .count();
    }

//trovo conversazioni tra due utenti

    public List<MessageResponseDTO> getConversation(UUID user1Id, UUID user2Id) {
        // Verifica che entrambi gli utenti esistano
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new NotFoundException(
                        "Utente con id " + user1Id + " non trovato"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new NotFoundException(
                        "Utente con id " + user2Id + " non trovato"));

        return messageRepository.findAll().stream()
                .filter(msg ->
                        (msg.getSender().getId().equals(user1Id) && msg.getReceiver().getId().equals(user2Id)) ||
                                (msg.getSender().getId().equals(user2Id) && msg.getReceiver().getId().equals(user1Id))
                )
                .sorted(Comparator.comparing(Message::getSentAt))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //trovo tutte le conversazioni di un utente
    public List<ConversationDTO> getAllConversations(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "Utente con id " + userId + " non trovato"));
        Set<UUID> conversationUserIds = new HashSet<>();
        messageRepository.findAll().forEach(msg -> {
            if (msg.getSender().getId().equals(userId)) {
                conversationUserIds.add(msg.getReceiver().getId());
            } else if (msg.getReceiver().getId().equals(userId)) {
                conversationUserIds.add(msg.getSender().getId());
            }
        });




//creo dto, cioè organizzo ogni conversazione tra utenti
     return conversationUserIds.stream()
             .map(otherUserId -> {
        User otherUser = userRepository.findById(otherUserId).orElse(null);
        if (otherUser == null) return null;

        List<MessageResponseDTO> messages = getConversation(userId, otherUserId);

        long unreadCount = messages.stream()
                .filter(msg -> !msg.isRead() && msg.receiver().id().equals(userId))
                .count();

        return new ConversationDTO(
                otherUserId,
                otherUser.getUsername(),
                otherUser.getProfileImage(),
                messages,
                (int) unreadCount
        );
    })
            .filter(Objects::nonNull)
                .sorted(Comparator.comparing((ConversationDTO conv) ->
            conv.messages().isEmpty() ? new Date(0) :
            conv.messages().get(conv.messages().size() - 1).sentAt()
                ).reversed())
            .collect(Collectors.toList());
}

        private MessageResponseDTO mapToResponseDTO(Message message) {
        UserMessageSummaryDTO senderSummary = new UserMessageSummaryDTO(
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getSender().getProfileImage()
        );

        UserMessageSummaryDTO receiverSummary = new UserMessageSummaryDTO(
                message.getReceiver().getId(),
                message.getReceiver().getUsername(),
                message.getReceiver().getProfileImage()
        );

        PostMessageSummaryDTO postSummary = null;
        if (message.getRelatedPost() != null) {
            postSummary = new PostMessageSummaryDTO(
                    message.getRelatedPost().getId(),
                    message.getRelatedPost().getContent()
            );
        }

        return new MessageResponseDTO(
                message.getId(),
                message.getContentMessage(),
                message.getSentAt(),
                message.isRead(),
                senderSummary,
                receiverSummary,
                postSummary
        );
    }
}

