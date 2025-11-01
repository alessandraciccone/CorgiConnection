package alessandraciccone.CorgiConnection.entities;


import jakarta.persistence.*;
import jdk.jfr.Enabled;

import java.util.UUID;

@Entity
@Table(name="corgi:info")
public class CorgiInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)

    private UUID id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String infoContent;

    @Enumerated(EnumType.STRING)
    private InfoCategory category;

    public CorgiInfo (){};

    public CorgiInfo(String title, String infoContent, InfoCategory category) {

        this.title = title;
        this.infoContent = infoContent;
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

    public InfoCategory getCategory() {
        return category;
    }

    public void setCategory(InfoCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CorgiInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", infoContent='" + infoContent + '\'' +
                ", category=" + category +
                '}';
    }
}
