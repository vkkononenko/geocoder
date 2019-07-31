package vkkononenko.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Region implements Serializable {

    @Id
    private String code;

    private String name;

    private Double lessLat;

    private Double biggerLat;

    private Double lessLon;

    private Double biggerLon;

    private String upperCorner;

    private String lowerCorner;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLessLat() {
        return lessLat;
    }

    public void setLessLat(Double lessLat) {
        this.lessLat = lessLat;
    }

    public Double getBiggerLat() {
        return biggerLat;
    }

    public void setBiggerLat(Double biggerLat) {
        this.biggerLat = biggerLat;
    }

    public Double getLessLon() {
        return lessLon;
    }

    public void setLessLon(Double lessLon) {
        this.lessLon = lessLon;
    }

    public Double getBiggerLon() {
        return biggerLon;
    }

    public void setBiggerLon(Double biggerLon) {
        this.biggerLon = biggerLon;
    }

    public String getUpperCorner() {
        return upperCorner;
    }

    public void setUpperCorner(String upperCorner) {
        this.upperCorner = upperCorner;
    }

    public String getLowerCorner() {
        return lowerCorner;
    }

    public void setLowerCorner(String loweCorner) {
        this.lowerCorner = loweCorner;
    }

    public boolean hasCoords() {
        return (this.biggerLat != null && this.biggerLon != null && this.lessLat != null && this.lessLon != null);
    }
}
