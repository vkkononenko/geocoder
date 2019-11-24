package vkkononenko.models.YandexMapsAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import vkkononenko.models.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PointsJSON implements Serializable {

    @JsonProperty("type")
    private String type;

    @JsonProperty("features")
    private List<Features> features;

    public PointsJSON(Stop stop) {
        type = "FeatureCollection";
        features = new ArrayList<>();
        features.add(new Features(stop));
    }


    public PointsJSON(Stop stop, Long resultId) {
        type = "FeatureCollection";
        features = new ArrayList<>();
        stop.getCoords().stream().filter((GeoCodingResult result) -> result.getId().equals(resultId)).findFirst().get()
        .getGeoCodingObjects().forEach((GeoCodingObject object) -> features.add(new Features(object)));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Features> getFeatures() {
        return features;
    }

    public void setFeatures(List<Features> features) {
        this.features = features;
    }
}