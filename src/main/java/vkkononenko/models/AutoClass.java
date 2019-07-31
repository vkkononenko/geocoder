package vkkononenko.models;

import vkkononenko.models.base.EntityBase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AutoClass extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String routeId;

    private Long OM;

    private Long M;

    private Long C;

    private Long B;

    private Long OB;

    private Long sum;

    private String ecoClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Long getOM() {
        return OM;
    }

    public void setOM(Long OM) {
        this.OM = OM;
    }

    public Long getM() {
        return M;
    }

    public void setM(Long m) {
        M = m;
    }

    public Long getC() {
        return C;
    }

    public void setC(Long c) {
        C = c;
    }

    public Long getB() {
        return B;
    }

    public void setB(Long b) {
        B = b;
    }

    public Long getOB() {
        return OB;
    }

    public void setOB(Long OB) {
        this.OB = OB;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public String getEcoClass() {
        return ecoClass;
    }

    public void setEcoClass(String ecoClass) {
        this.ecoClass = ecoClass;
    }
}
