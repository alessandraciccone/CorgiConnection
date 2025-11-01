package alessandraciccone.CorgiConnection.entities;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="post_photos")
public class PostPhoto {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    @Lob
    @Column(nullable = false)
    private String imageUrl;


    public PostPhoto(){};

    public PostPhoto(String imageUrl) {

        this.imageUrl = imageUrl;

    }

    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    public UUID getId() {
        return id;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "PostPhoto{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
