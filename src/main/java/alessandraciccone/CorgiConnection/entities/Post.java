package alessandraciccone.CorgiConnection.entities;
import jakarta.persistence.*;
import alessandraciccone.CorgiConnection.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable=false, length = 1000)
    private String content;

private LocalDate datePost;

    public Post(){};

    public Post(String content, LocalDate datePost, User author, List<PostPhoto> photos, List<Comment> comments) {
        this.content = content;
        this.datePost= datePost;
        this.author = author;
        this.photos = photos;
        this.comments = comments;
    }

    @ManyToOne
    @JoinColumn(name="author_id", nullable = false)
    private User author;



   @OneToMany(mappedBy = "post", cascade= CascadeType.ALL)
    private List<PostPhoto> photos;

   @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public UUID getId() {
        return id;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDatePost() {
        return datePost;
    }

    public void setDatePost(LocalDate datePost) {
        this.datePost = datePost;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }



    public List <PostPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PostPhoto> photos) {
        this.photos = photos;
    }

    public List <Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override

    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", datePost=" + datePost +
                '}';
    }
}
