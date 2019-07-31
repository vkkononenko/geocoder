package vkkononenko.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import vkkononenko.models.YandexGeocoder.Components;
import vkkononenko.models.YandexGeocoder.FeatureMember;
import vkkononenko.models.base.EntityBase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class GeoCodingObject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", insertable = true, updatable = false)
    @JsonIgnore
    protected Date dateCreated;

    private String description;

    private String name;

    private String type;

    private String precision;

    private String postal_code;

    private String formatted;

    @Column(columnDefinition = "TEXT")
    private String text;

    private Double lat;

    private Double lon;

    private Double latLowerCorner;

    private Double lonLowerCorner;

    private Double latUpperCorner;

    private Double lonUpperCorner;

    private String sendedRequest;

    private String adress;

    private String okato;

    @Transient
    @OneToMany
    private List<Component> components;

    public GeoCodingObject(FeatureMember featureMember) {
        this.dateCreated = new Date();
        this.name = featureMember.getGeoObjectYandex().getName();
        this.description = featureMember.getGeoObjectYandex().getDescription();
        this.type = featureMember.getGeoObjectYandex().getMetaDataProperty().getGeocoderMetaData().getKind();
        this.postal_code = featureMember.getGeoObjectYandex().getMetaDataProperty().getGeocoderMetaData().getAddress().getPostal_code();
        this.formatted = featureMember.getGeoObjectYandex().getMetaDataProperty().getGeocoderMetaData().getAddress().getFormatted();
        this.text = featureMember.getGeoObjectYandex().getMetaDataProperty().getGeocoderMetaData().getText();
        this.lat = Double.parseDouble(featureMember.getGeoObjectYandex().getPoint().getPos().split(" ")[0]);
        this.lon = Double.parseDouble(featureMember.getGeoObjectYandex().getPoint().getPos().split(" ")[1]);
        this.latLowerCorner = Double.parseDouble(featureMember.getGeoObjectYandex().getBoundedBy().getEnvelope().getLowerCorner().split(" ")[0]);
        this.lonLowerCorner = Double.parseDouble(featureMember.getGeoObjectYandex().getBoundedBy().getEnvelope().getLowerCorner().split(" ")[1]);
        this.latUpperCorner = Double.parseDouble(featureMember.getGeoObjectYandex().getBoundedBy().getEnvelope().getUpperCorner().split(" ")[0]);
        this.lonUpperCorner = Double.parseDouble(featureMember.getGeoObjectYandex().getBoundedBy().getEnvelope().getUpperCorner().split(" ")[1]);
        this.components = new ArrayList<>();
        for (Components component : featureMember.getGeoObjectYandex().getMetaDataProperty().getGeocoderMetaData().getAddress().getComponents()) {
            this.components.add(new Component(component));
        }
    }

    public GeoCodingObject() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLatLowerCorner() {
        return latLowerCorner;
    }

    public void setLatLowerCorner(Double latLowerCorner) {
        this.latLowerCorner = latLowerCorner;
    }

    public Double getLonLowerCorner() {
        return lonLowerCorner;
    }

    public void setLonLowerCorner(Double lonLowerCorner) {
        this.lonLowerCorner = lonLowerCorner;
    }

    public Double getLatUpperCorner() {
        return latUpperCorner;
    }

    public void setLatUpperCorner(Double latUpperCorner) {
        this.latUpperCorner = latUpperCorner;
    }

    public Double getLonUpperCorner() {
        return lonUpperCorner;
    }

    public void setLonUpperCorner(Double lonUpperCorner) {
        this.lonUpperCorner = lonUpperCorner;
    }

    public String getSendedRequest() {
        return sendedRequest;
    }

    public void setSendedRequest(String sendedRequest) {
        this.sendedRequest = sendedRequest;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getOkato() {
        return okato;
    }

    public void setOkato(String okato) {
        this.okato = okato;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
}
