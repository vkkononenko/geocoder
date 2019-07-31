package vkkononenko.models.YandexGeocoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import vkkononenko.models.YandexGeocoder.Emb.MetaDataProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoObjectCollection implements Serializable {
    @Embedded
    @JsonProperty("metaDataProperty")
    private MetaDataProperty metaDataProperty;

    @JsonProperty("featureMember")
    private List<FeatureMember> featureMember = null;

    public MetaDataProperty getMetaDataProperty() {
        return metaDataProperty;
    }

    public void setMetaDataProperty(MetaDataProperty metaDataProperty) {
        this.metaDataProperty = metaDataProperty;
    }

    public List<FeatureMember> getFeatureMember() {
        return featureMember;
    }

    public void setFeatureMember(List<FeatureMember> featureMember) {
        this.featureMember = featureMember;
    }
}
