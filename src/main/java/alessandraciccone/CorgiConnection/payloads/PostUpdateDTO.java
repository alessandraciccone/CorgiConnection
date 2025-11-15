package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public record PostUpdateDTO(

        @Size(max=1000, message="Il contenuto non può superare i 1000 caratteri!")
        String content

        ) {}
