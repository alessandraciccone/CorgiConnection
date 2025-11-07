package alessandraciccone.CorgiConnection.payloads;

import alessandraciccone.CorgiConnection.entities.ThingsType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record PetFriendlyThingsDTO(

        @NotBlank(message="Il nome del luogo è obbligatorio!")
        @Size(min=2, max=200, message="Il nome deve avere tra i due e i 200 caratteri!")
String petFriendlyName,

        @NotNull(message="Il tipo è obbligatorio!")
        ThingsType type,

        String descriptionThing,

        String address,

       @NotBlank(message="La città è obbligatoria!")
        String cityThing,
     String districtThing,

        String region,

        @Future(message="La data dell'evento deve essere futura!")

        Date eventTime

) {
}
