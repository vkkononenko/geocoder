package vkkononenko.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import vkkononenko.models.YandexGeocoder.Components;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Component implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", insertable = true, updatable = false)
    @JsonIgnore
    protected Date dateCreated;

    protected String kind;

    protected String name;

    public Component(Components components) {
        this.dateCreated = new Date();
        this.kind = components.getKind();
        this.name = components.getName();
    }

    public Component() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
