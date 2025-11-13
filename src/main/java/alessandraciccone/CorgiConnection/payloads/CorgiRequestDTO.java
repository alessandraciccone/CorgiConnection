package alessandraciccone.CorgiConnection.payloads;



import alessandraciccone.CorgiConnection.entities.CorgiType;
import alessandraciccone.CorgiConnection.entities.Gender;

import java.util.UUID;

public record CorgiRequestDTO(
        String name,
        Integer age,
        Gender gender,
        CorgiType type,
        String color,
        String personality,
        String photo,
        UUID ownerId
) {}