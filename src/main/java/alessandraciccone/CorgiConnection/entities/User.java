package alessandraciccone.CorgiConnection.entities;


import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true) //unique ossia valore iunico nel db
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    private String firstName;
    private String lastName;
    private String city;
    private String province;


    @Lob// ideale per immagini o dati di grandi dimensioni
    private String profileImage;


    public User() {
    }

    ;

    public User( String username, String email, String password, String firstName, String lastName, String city, String province,String profileImage, List<Corgi> corgis, List<Post> posts, List<Comment> commenti, List<Message> sentMessages, List<Message> receivedMessages, List<QuizResult> quizResult) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.province = province;
        this.profileImage=profileImage;
        this.corgis = corgis;
        this.posts = posts;
        this.commenti = commenti;
        this.sentMessages = sentMessages;
        this.receivedMessages = receivedMessages;
        this.quizResult = quizResult;
    }


    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Corgi> corgis;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> commenti;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> receivedMessages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<QuizResult> quizResult;



    public void setId(UUID id) {
        this.id = id;
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public List<Corgi> getCorgis() {
        return corgis;
    }

    public void setCorgis(List<Corgi> corgis) {
        this.corgis = corgis;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getCommenti() {
        return commenti;
    }

    public void setCommenti(List<Comment> commenti) {
        this.commenti = commenti;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public List<QuizResult> getQuizResult() {
        return quizResult;
    }

    public void setQuizResult(List<QuizResult> quizResult) {
        this.quizResult = quizResult;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
