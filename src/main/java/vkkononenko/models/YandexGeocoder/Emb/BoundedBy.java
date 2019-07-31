package vkkononenko.models.YandexGeocoder.Emb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import vkkononenko.models.YandexGeocoder.Envelope;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundedBy {

    @Embedded
    @JsonProperty("Envelope")
    private Envelope envelope;

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }
}
