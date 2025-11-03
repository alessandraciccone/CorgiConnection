package alessandraciccone.CorgiConnection.entities;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name="pet_friendly_things")
public class PetFriendlyThings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String petFriendlyName;

    @Enumerated(EnumType.STRING)
    private ThingsType type;

    @Lob
    private String descriptionThing;

    private String address;
    private String cityThing;
    private String districtThing;
    private String region;

    @Temporal(TemporalType.DATE)
    private Date eventTime;

    private Boolean isActive=true;

    public PetFriendlyThings(){};

    public PetFriendlyThings( String petFriendlyName, ThingsType type, String descriptionThing, String address, String cityThing, String districtThing, String region, Date eventTime, Boolean isActive) {
        this.petFriendlyName = petFriendlyName;
this.type=type;
this.descriptionThing = descriptionThing;
        this.address = address;
        this.cityThing = cityThing;
        this.districtThing = districtThing;
        this.region = region;
        this.eventTime = eventTime;
        this.isActive = isActive;
    }


    public UUID getId() {
        return id;
    }

    public String getPetFriendlyName() {
        return petFriendlyName;
    }

    public void setPetFriendlyName(String petFriendlyName) {
        this.petFriendlyName = petFriendlyName;
    }

    public ThingsType getType() {
        return type;
    }

    public void setType(ThingsType type) {
        this.type = type;
    }

    public String getDescriptionThing() {
        return descriptionThing;
    }

    public void setDescriptionThing(String descriptionThing) {
        this.descriptionThing = descriptionThing;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityThing() {
        return cityThing;
    }

    public void setCityThing(String cityThing) {
        this.cityThing = cityThing;
    }

    public String getDistrictThing() {
        return districtThing;
    }

    public void setDistrictThing(String districtThing) {
        this.districtThing = districtThing;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "PetFriendlyThings{" +
                "id=" + id +
                ", petFriendlyName='" + petFriendlyName + '\'' +
                ", type=" + type +
                ", descriptionThing='" + descriptionThing + '\'' +
                ", address='" + address + '\'' +
                ", cityThing='" + cityThing + '\'' +
                ", districtThing='" + districtThing + '\'' +
                ", region='" + region + '\'' +
                ", eventTime=" + eventTime +
                ", isActive=" + isActive +
                '}';
    }
}
