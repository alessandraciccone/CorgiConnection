package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @Size(min=3, max=50, message="L'username deve avere tra i 3 e i 50 caratteri!")
        String username,
        @Email(message="Il formato dell'email non è valido")
        String email,

        String firstName,
        String lastName,
        String city,
        String province,
        String profileImage
)
{}
