package alessandraciccone.CorgiConnection.payloads;

import alessandraciccone.CorgiConnection.entities.Gender;

import java.util.UUID;

public record CorgiSummaryDTO(
        UUID id,
        String name,
        Integer age,
        Gender gender,
        String photo


) {
}
