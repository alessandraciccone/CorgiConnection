package alessandraciccone.CorgiConnection.payloads;

import alessandraciccone.CorgiConnection.entities.ThingsType;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record PetFriendlyThingsUpdateDTO(

        @Size(min=2, max=200, message="Il nome deve esserre tra i 2 e i 200 caratteri")
        String petFriendlyName,

        ThingsType type,

        String descriptionThing,

        String address,

        String cityThing,

        String districtThing,

        String region,

        Date eventTime,

        Boolean isActive
) {}
