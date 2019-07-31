package vkkononenko.models;

import vkkononenko.models.base.EntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Route extends EntityBase {

    @Id
    private String id;

    private Long seqNum;

    private String routeName;

    private String carrierName;

    private String bardingType;

    private String trafficType;

    private String forwardNameDirection;

    private String backwarNamedDirection;

    private Long forwardLengthDirection;

    private Long backwardLengthDirection;

    private Date startDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Long seqNum) {
        this.seqNum = seqNum;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getBardingType() {
        return bardingType;
    }

    public void setBardingType(String bardingType) {
        this.bardingType = bardingType;
    }

    public String getTrafficType() {
        return trafficType;
    }

    public void setTrafficType(String trafficType) {
        this.trafficType = trafficType;
    }

    public String getForwardNameDirection() {
        return forwardNameDirection;
    }

    public void setForwardNameDirection(String forwardNameDirection) {
        this.forwardNameDirection = forwardNameDirection;
    }

    public String getBackwarNamedDirection() {
        return backwarNamedDirection;
    }

    public void setBackwarNamedDirection(String backwarNamedDirection) {
        this.backwarNamedDirection = backwarNamedDirection;
    }

    public Long getForwardLengthDirection() {
        return forwardLengthDirection;
    }

    public void setForwardLengthDirection(Long forwardLengthDirection) {
        this.forwardLengthDirection = forwardLengthDirection;
    }

    public Long getBackwardLengthDirection() {
        return backwardLengthDirection;
    }

    public void setBackwardLengthDirection(Long backwardLengthDirection) {
        this.backwardLengthDirection = backwardLengthDirection;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
