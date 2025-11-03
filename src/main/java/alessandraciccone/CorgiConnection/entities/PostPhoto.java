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

@Column
private String captionPhoto;


    public PostPhoto(){};

    public PostPhoto(String imageUrl,String captionPhoto) {

        this.imageUrl = imageUrl;
        this.captionPhoto=captionPhoto;

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

    public String getCaptionPhoto() {
        return captionPhoto;
    }

    public void setCaptionPhoto(String captionPhoto) {
        this.captionPhoto = captionPhoto;
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
                ", captionPhoto='" + captionPhoto + '\'' +
                '}';
    }
}
