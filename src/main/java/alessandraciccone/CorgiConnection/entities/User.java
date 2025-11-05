package alessandraciccone.CorgiConnection.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String city;
    private String province;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;



    @Column(name = "profile_image")
    private String profileImage;

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

    public User() {}

<<<<<<< Updated upstream
    public User(String username, String email, String password, String firstName, String lastName,
                String city, String province, Date registrationDate, String profileImage,
                List<Corgi> corgis, List<Post> posts, List<Comment> commenti,
                List<Message> sentMessages, List<Message> receivedMessages, List<QuizResult> quizResult) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.province = province;
        this.registrationDate = registrationDate;
        this.profileImage=profileImage;
        this.corgis = corgis;
        this.posts = posts;
        this.commenti = commenti;
        this.sentMessages = sentMessages;
        this.receivedMessages = receivedMessages;
        this.quizResult = quizResult;
<<<<<<< Updated upstream
=======
=======
    public UUID getId() {
        return id;
>>>>>>> Stashed changes
>>>>>>> Stashed changes
    }

    public UUID getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }



    public void setCorgis(List<Corgi> corgis) {
        this.corgis = corgis;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public void setCommenti(List<Comment> commenti) {
        this.commenti = commenti;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public void setQuizResult(List<QuizResult> quizResult) {
        this.quizResult = quizResult;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public Date getRegistrationDate() {
        return registrationDate;
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

    public List<Post> getPosts() {
        return posts;
    }

    public List<Comment> getCommenti() {
        return commenti;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public List<QuizResult> getQuizResult() {
        return quizResult;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // -------------------- UserDetails methods --------------------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Per ora non gestiamo ruoli, restituiamo lista vuota
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
