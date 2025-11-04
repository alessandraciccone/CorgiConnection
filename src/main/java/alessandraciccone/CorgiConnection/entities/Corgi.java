package alessandraciccone.CorgiConnection.entities;
import jakarta.persistence.*;
import alessandraciccone.CorgiConnection.entities.User;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="corgis")
public class Corgi {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @Column(nullable = false)
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private CorgiType type;

    private String color;
    private String personality;


    private String photo;

    public Corgi() {
    }

    ;

    public Corgi(String name, Integer age, Gender gender, CorgiType type, String color, String personality, String photo, User owner, List<Post> posts) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.type = type;
        this.color = color;
        this.personality = personality;
        this.photo = photo;
        this.owner = owner;
        this.posts = posts;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "corgi", cascade = CascadeType.ALL)

    private List<Post> posts;

    public UUID getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public CorgiType getType() {
        return type;
    }

    public void setType(CorgiType type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "Corgi{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", type=" + type +
                ", color='" + color + '\'' +
                ", personality='" + personality + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
