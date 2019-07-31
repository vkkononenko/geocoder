package vkkononenko.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import vkkononenko.models.YandexGeocoder.FeatureMember;
import vkkononenko.models.YandexGeocoder.GeoObjectYandex;
import vkkononenko.models.YandexGeocoder.StopCoordsYandex;
import vkkononenko.models.base.EntityBase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class GeoCodingResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", insertable = true, updatable = false)
    protected Date dateCreated;

    private String sendedRequest;

    private String receivedRequest;

    private Long found;

    private Long count = 0L;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GeoCodingObject> geoCodingObjects;

    public GeoCodingResult() {

    }

    public GeoCodingResult(StopCoordsYandex yandex, String sendedRequest) {
        this.dateCreated = new Date();
        this.geoCodingObjects = new ArrayList<>();
        this.sendedRequest = sendedRequest;
        this.receivedRequest = yandex.getRequest();
        this.found = Long.parseLong(yandex.getResponse().getGeoObjectCollection().getMetaDataProperty().getGeocoderResponseMetaData().getFound());
        for (FeatureMember featureMember : yandex.getResponse().getGeoObjectCollection().getFeatureMember()) {
            GeoCodingObject object = new GeoCodingObject(featureMember);
            this.geoCodingObjects.add(object);
        }
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

    public String getSendedRequest() {
        return sendedRequest;
    }

    public void setSendedRequest(String sendedRequest) {
        this.sendedRequest = sendedRequest;
    }

    public String getReceivedRequest() {
        return receivedRequest;
    }

    public void setReceivedRequest(String receivedRequest) {
        this.receivedRequest = receivedRequest;
    }

    public Long getFound() {
        return found;
    }

    public void setFound(Long found) {
        this.found = found;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<GeoCodingObject> getGeoCodingObjects() {
        return geoCodingObjects;
    }

    public void setGeoCodingObjects(List<GeoCodingObject> geoCodingObjects) {
        this.geoCodingObjects = geoCodingObjects;
    }
}
