package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.Size;

public record MessageUpdateDTO(

        @Size( max=1000,message="Il messaggio non può superare i mille caratteri!")
        String contentMessage,
        Boolean isRead
){}
