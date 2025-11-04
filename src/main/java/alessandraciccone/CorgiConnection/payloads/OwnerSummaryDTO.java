package alessandraciccone.CorgiConnection.payloads;
import java.util.UUID;
// aggiungo questo record per evitare riferimenti circolari. Due oggetti in questo caso user e corgi si rimanderebbero a vincenda creando un loop
//è un problema quando gli orggetti bengono trasformati in json x le api



public record OwnerSummaryDTO(


        UUID id,
        String username, String city,
       String profileImage
) {
}
