package alessandraciccone.CorgiConnection.repositories;

import alessandraciccone.CorgiConnection.entities.Post;
import alessandraciccone.CorgiConnection.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {



List<Post> findByAuthor(User author);

List <Post> findByAuthor_Id(UUID author_Id);

List<Post> findByDatePost(LocalDate date);

long countByAuthor(User author);

List <Post> findByDatePostAfter(LocalDate date);
List <Post> findByDatePostBefore(LocalDate date);
List <Post> findByDatePostBetween(LocalDate startDate, LocalDate endDate);

@Query("SELECT p FROM Post p WHERE p.author.city = :city")
List<Post> findPostsByAuthorCity(@Param("city") String city);
@Query
        ("SELECT p FROM Post p WHERE p.datePost>= :date ORDER BY p.datePost DESC")
    List<Post> findRecentPosts(@Param("date")LocalDate date);

@Query
        ("SELECT p FROM Post p WHERE size(p.photos)>0")
        List<Post> findPostsWitPhotos();

@Query
        ("SELECT p FROM Post p WHERE SIZE(p.comments) >0")
    List<Post> findPostsWithComments();

@Query
        ("SELECT COUNT(p) FROM Post p WHERE p.author.city= :city")
    long countPostsByCity(@Param("city")String city);



}
