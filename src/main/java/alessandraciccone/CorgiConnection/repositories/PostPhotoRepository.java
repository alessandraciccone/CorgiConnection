package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostPhotoRepository extends JpaRepository<PostPhoto, UUID> {
}
