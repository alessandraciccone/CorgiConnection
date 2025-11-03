package alessandraciccone.CorgiConnection.payloads;
import alessandraciccone.CorgiConnection.entities.ThingsType;

import java.util.Date;
import java.util.UUID;
public record PetFriendlyThingsResponseDTO(

        UUID id,
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
