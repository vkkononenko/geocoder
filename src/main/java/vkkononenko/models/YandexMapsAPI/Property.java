package vkkononenko.models.YandexMapsAPI;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Property implements Serializable {

    @JsonProperty("balloonContentHeader")
    private String balloonContentHeader;

    @JsonProperty("balloonContentBody")
    private String balloonContentBody;

    public String getBalloonContentHeader() {
        return balloonContentHeader;
    }

    public void setBalloonContentHeader(String balloonContentHeader) {
        this.balloonContentHeader = balloonContentHeader;
    }

    public String getBalloonContentBody() {
        return balloonContentBody;
    }

    public void setBalloonContentBody(String balloonContentBody) {
        this.balloonContentBody = balloonContentBody;
    }
}
