package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record PostDTO(

        @NotBlank(message = "Il contenuto del post è obbligatorio!")
        @Size(max = 1000, message = "Il contenuto non può superare i 1000 caratteri")
        String content
) {}

