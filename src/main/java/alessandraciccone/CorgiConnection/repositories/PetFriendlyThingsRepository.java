package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.PetFriendlyThings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PetFriendlyThingsRepository extends JpaRepository<PetFriendlyThings, UUID>, JpaSpecificationExecutor<PetFriendlyThings> {
}
