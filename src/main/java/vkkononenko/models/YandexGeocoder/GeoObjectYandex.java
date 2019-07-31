package vkkononenko.models.YandexGeocoder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import vkkononenko.models.YandexGeocoder.Emb.BoundedBy;
import vkkononenko.models.YandexGeocoder.Emb.Point;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoObjectYandex implements Serializable {

    @JsonProperty("description")
    private String description;

    @JsonProperty("name")
    private String name;

    @Embedded
    @JsonProperty("boundedBy")
    private BoundedBy boundedBy;

    @Embedded
    @JsonProperty("Point")
    private Point point;

    @JsonProperty("metaDataProperty")
    private MetaDataProperty metaDataProperty;

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

    public BoundedBy getBoundedBy() {
        return boundedBy;
    }

    public void setBoundedBy(BoundedBy boundedBy) {
        this.boundedBy = boundedBy;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public MetaDataProperty getMetaDataProperty() {
        return metaDataProperty;
    }

    public void setMetaDataProperty(MetaDataProperty metaDataProperty) {
        this.metaDataProperty = metaDataProperty;
    }


}
