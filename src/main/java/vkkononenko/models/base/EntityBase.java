package vkkononenko.models.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class EntityBase implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", insertable = true, updatable = false)
    protected Date dateCreated;

    @PrePersist
    protected void init(){
        dateCreated = new Date();
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
