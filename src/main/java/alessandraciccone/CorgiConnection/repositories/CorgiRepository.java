package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Corgi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CorgiRepository extends JpaRepository<Corgi, UUID> {
}
