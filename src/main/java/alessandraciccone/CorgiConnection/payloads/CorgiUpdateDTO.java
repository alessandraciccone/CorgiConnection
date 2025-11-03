package alessandraciccone.CorgiConnection.payloads;

import alessandraciccone.CorgiConnection.entities.CorgiType;
import alessandraciccone.CorgiConnection.entities.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CorgiUpdateDTO(
        @Size(min=2, max=50, message="Il nome del cagnolino deve avere tra i 2 e i 50 caratteri!")
        String name,

        @Min(value=0,message="l'età non deve essere negativa")
@Max( value=25, message="l'età non deve superare i 25 anni!")
Integer age,

        Gender gender,
        CorgiType type,
        String color,
        String personality,
        String photo

) {
}
