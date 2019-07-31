package vkkononenko.models.YandexGeocoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureMember implements Serializable {

    @JsonProperty("GeoObject")
    @OneToOne
    private GeoObjectYandex geoObjectYandex;

    public GeoObjectYandex getGeoObjectYandex() {
        return geoObjectYandex;
    }

    public void setGeoObjectYandex(GeoObjectYandex geoObjectYandex) {
        this.geoObjectYandex = geoObjectYandex;
    }
}
