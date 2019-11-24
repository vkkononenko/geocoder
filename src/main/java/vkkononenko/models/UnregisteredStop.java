package vkkononenko.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class UnregisteredStop implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String regNum;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL)
    private List<GeoCodingResult> coords;

    @OneToMany(cascade = CascadeType.ALL)
    private List<StopAdress> adress;

    public UnregisteredStop() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public List<GeoCodingResult> getCoords() {
        return coords;
    }

    public void setCoords(List<GeoCodingResult> coords) {
        this.coords = coords;
    }

    public List<StopAdress> getAdress() {
        return adress;
    }

    public void setAdress(List<StopAdress> adress) {
        this.adress = adress;
    }
}
