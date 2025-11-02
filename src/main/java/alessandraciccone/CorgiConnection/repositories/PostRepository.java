package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Gender;
import alessandraciccone.CorgiConnection.entities.Post;
import alessandraciccone.CorgiConnection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {



List<Post> findByAuthor(User author);

List <Post> findByAuthor_Id(UUID author_Id);

List<Post> findByDatePost(LocalDate date);

long countByAuthor(User author);


}
