package vkkononenko.models.YandexMapsAPI;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Options implements Serializable {

    @JsonProperty("preset")
    private String preset = "islands#violetDotIcon";

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }
}
