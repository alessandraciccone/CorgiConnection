package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

   //uso optional. la classe optionale può contenere un valore come non può contenerlo. gestisce l'assenza di valore in modo più sicuro rispetto a null
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    //verifico se esistono gli utenti
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
