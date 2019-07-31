package vkkononenko.models;

import vkkononenko.models.base.EntityBase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Stop extends EntityBase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String name; //Наименование остановочного пункта

    private String regNum; //Регистрационный номер остановочного пункта

    private Date regDate; //Дата регистрации

    @Column(columnDefinition = "TEXT")
    private String place; //Место нахождения

    @Column(columnDefinition = "TEXT")
    private String ownerName; //Наименование владельца

    private Long capasity; //Пропускная способность, отправлений/час

    private Long timeBreak; //Время перерывов технологического характера, мин.

    @Column(columnDefinition = "TEXT")
    private String path; //Путь подъезда транспортных средств к остановочному пункту

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<GeoCodingResult> coords;

    private Boolean alive;

    private String request;

    private Double lat;

    private Double lon;

    private String adress;

    private String okato;

    private Long count;

    private String type;

    private Double maxDistance;

    private String sourse;

    public Stop() {

    }

    public Stop(String name, String regNum, Date regDate, String place, String ownerName, Long capasity, Long timeBreak, String path) {
        this.name = name;
        this.regNum = regNum;
        this.regDate = regDate;
        this.place = place;
        this.ownerName = ownerName;
        this.capasity = capasity;
        this.timeBreak = timeBreak;
        this.path = path;
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

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getCapasity() {
        return capasity;
    }

    public void setCapasity(Long capasity) {
        this.capasity = capasity;
    }

    public Long getTimeBreak() {
        return timeBreak;
    }

    public void setTimeBreak(Long timeBreak) {
        this.timeBreak = timeBreak;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<GeoCodingResult> getCoords() {
        return coords;
    }

    public void setCoords(List<GeoCodingResult> coords) {
        this.coords = coords;
    }

    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getOkato() {
        return okato;
    }

    public void setOkato(String okato) {
        this.okato = okato;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(Double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getSourse() {
        return sourse;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
    }
}
