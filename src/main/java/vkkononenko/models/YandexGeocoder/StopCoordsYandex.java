package vkkononenko.models.YandexGeocoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import vkkononenko.models.YandexGeocoder.Response;
import vkkononenko.models.base.EntityBase;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StopCoordsYandex extends EntityBase {

    @JsonIgnore
    private String request;

    @JsonProperty("response")
    private Response response;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
