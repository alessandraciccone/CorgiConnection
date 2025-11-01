package alessandraciccone.CorgiConnection.entities;
import jakarta.persistence.*;
import alessandraciccone.CorgiConnection.entities.User;
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


    public Post(){};

    public Post(String content, User author, Corgi corgi, List<PostPhoto> photos, List<Comment> comments) {
        this.content = content;
        this.author = author;
        this.corgi = corgi;
        this.photos = photos;
        this.comments = comments;
    }

    @ManyToOne
    @JoinColumn(name="author_id", nullable = false)
    private User author;

   @ManyToOne
    @JoinColumn(name="corgi_id")
    private Corgi corgi;

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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Corgi getCorgi() {
        return corgi;
    }

    public void setCorgi(Corgi corgi) {
        this.corgi = corgi;
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
                ", author=" + author +
                ", corgi=" + corgi + +
                '}';
    }
}
