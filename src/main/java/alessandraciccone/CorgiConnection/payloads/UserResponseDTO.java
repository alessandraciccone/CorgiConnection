package alessandraciccone.CorgiConnection.payloads;

import java.util.Date;
import java.util.UUID;

//uso response per restituire dati al cliente e per controllare cosa esporre(ad esempio la password non viene mai mostrata

public record UserResponseDTO (
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        String city,
        String province,
        Date registrationDate,
        String profileImage

//è diverso da UserDTO che uso per creare o aggiornare un utente user response restituisce i dati (get)
){}
