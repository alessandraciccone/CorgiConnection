package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface   UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

//optional serve per gestire il caso in cui l'utente non venga trovato nel database lo gestisce in modo elegante senza restituire null
Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    //verifico se esistono gli utenti
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
//la repository è l'interfaccia che permette di interagire con il database. estende JpaRepository che fornisce metodi CRUD e JpaSpecificationExecutor per query complesse