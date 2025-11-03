package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Corgi;
import alessandraciccone.CorgiConnection.entities.CorgiType;
import alessandraciccone.CorgiConnection.entities.Gender;
import alessandraciccone.CorgiConnection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.UUID;

public interface CorgiRepository extends JpaRepository<Corgi, UUID> {


List<Corgi> findByOwner(User owner);

List<Corgi> findByOwner_Id(UUID owner_Id);

List<Corgi> findByGender(Gender gender);

List<Corgi> findByType(CorgiType type);
List<Corgi> findByAge(Integer age);
List<Corgi> findByAgeBetween( Integer minAge, Integer maxAge);
List<Corgi> findByNameContainingIgnoreCase(String name);
long countByOwner_Id(UUID owner_Id);

@Query("SELECT c FROM Corgi c WHERE c.owner.city=: city")
    List<Corgi> findCorgisByOwnerCity(@Param("city")String city);


}
