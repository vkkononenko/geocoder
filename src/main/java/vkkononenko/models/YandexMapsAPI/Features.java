package vkkononenko.models.YandexMapsAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import vkkononenko.models.GeoCodingObject;
import vkkononenko.models.Stop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Features implements Serializable {

    @JsonProperty("type")
    private String type;

    @JsonProperty("geometry")
    private Geometry geometry;

    @JsonProperty("properties")
    private Property property;

    @JsonProperty("options")
    private Options options;

    public Features(Stop stop) {
        type = "Feature";

        options = new Options();
        geometry = new Geometry();
        property = new Property();

        List<Double> coordinates = new ArrayList<>();

        if(stop.getSourse().equals("yandex")) {
            coordinates.add(stop.getLon());
            coordinates.add(stop.getLat());
        } else {
            coordinates.add(stop.getLat());
            coordinates.add(stop.getLon());
        }

        geometry.setType("Point");
        geometry.setCoordinates(coordinates);
        property.setBalloonContentHeader(stop.getOkato());
        property.setBalloonContentBody(stop.getAdress());
    }

    public Features(GeoCodingObject object) {
        type = "Feature";

        options = new Options();
        geometry = new Geometry();
        property = new Property();


        List<Double> coordinates = new ArrayList<>();
        coordinates.add(object.getLon());
        coordinates.add(object.getLat());
        geometry.setType("Point");
        geometry.setCoordinates(coordinates);

        property.setBalloonContentHeader(object.getSendedRequest());
        property.setBalloonContentBody(object.getAdress());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }


}