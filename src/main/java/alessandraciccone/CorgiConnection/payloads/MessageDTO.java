package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MessageDTO(

        @NotBlank(message="il contenuto del messaggio è obbligatorio!")
        @Size(max=1000, message="il messaggio può avere massimo mille caratteri!")
        String contentMessage,

        @NotNull(message="l'Id del destinatario è obbligatorio")
        UUID receiver_Id,

        @NotNull(message="L'id del mittente è obbligatorio!")
        UUID sender_Id,

        UUID relatedPost_Id
) {}
