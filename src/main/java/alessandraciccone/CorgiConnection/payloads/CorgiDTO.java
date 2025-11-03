package alessandraciccone.CorgiConnection.payloads;

import alessandraciccone.CorgiConnection.entities.CorgiType;
import alessandraciccone.CorgiConnection.entities.Gender;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CorgiDTO(
        @NotBlank(message="Il nome del cagnolino è obbligatorio!")
        @Size(min=2, max=50, message="Il nome deve avere tra i due e i 50 caratteri")
        String name,


@Min(value=0, message="l'età non può essere negativa!")
@Max(value=25,message="l'età non può superare i 25 anni!")
Integer age,

        @NotNull(message="il genere è obbligatorio!")
        Gender gender,

        @NotNull(message="il tipo di corgi è obbligatorio!")
        CorgiType type,

        String color,
        String personality,
        String photo,
        @NotNull(message="L'id del proprietario è obbligatorio!")
        UUID owner_Id

) {
}
