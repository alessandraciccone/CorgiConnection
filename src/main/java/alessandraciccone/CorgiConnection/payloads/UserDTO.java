package alessandraciccone.CorgiConnection.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
//DTO--> DATA TRANSFER OBJECT
//uso userDto x ricevere dati in imput, ad esempio duramnte la registrazione
public record UserDTO (
        @NotBlank(message="L'username è obbligatorio!")//registraziomne utente username non blank
        @Size(min = 3, max=50, message="L'username deve avere tra i 3 e i 50 caratteri!")
        String username,

        @NotBlank(message="L'email è obbligatoria!")
        @Email(message="Il formato dell'email non è valido")
        String email,

        @NotBlank(message="La password è obbligatoria!")
        @Size(min=6, message="La password deve avere almeno 6 caratteri")
        String password,
        String firstName,
        String lastName,
        String city,
        String province,
        String profileImage

//non si usa entity User perche cosi si evita di esporre dati sensibili come la password
){}
