package com.btapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Historybtr.
 */
@Entity
@Table(name = "historybtr")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "historybtr")
public class Historybtr implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "btrstatusbefore", nullable = false)
    private String btrstatusbefore;
    
    @NotNull
    @Column(name = "btrstatusafter", nullable = false)
    private String btrstatusafter;
    
    @NotNull
    @Column(name = "change_date", nullable = false)
    private ZonedDateTime change_date;
    
    @NotNull
    @Column(name = "changed_by", nullable = false)
    private String changed_by;
    
    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime start_date;
    
    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime end_date;
    
    @NotNull
    @Column(name = "assigned_to", nullable = false)
    private String assigned_to;
    
    @NotNull
    @Column(name = "assigned_from", nullable = false)
    private String assigned_from;
    
    @NotNull
    @Column(name = "location", nullable = false)
    private String location;
    
    @NotNull
    @Column(name = "center_cost", nullable = false)
    private String center_cost;
    
    @NotNull
    @Column(name = "request_date", nullable = false)
    private ZonedDateTime request_date;
    
    @NotNull
    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime last_modified_date;
    
    @ManyToOne
    @JoinColumn(name = "btr_id")
    private Btr btr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBtrstatusbefore() {
        return btrstatusbefore;
    }
    
    public void setBtrstatusbefore(String btrstatusbefore) {
        this.btrstatusbefore = btrstatusbefore;
    }

    public String getBtrstatusafter() {
        return btrstatusafter;
    }
    
    public void setBtrstatusafter(String btrstatusafter) {
        this.btrstatusafter = btrstatusafter;
    }

    public ZonedDateTime getChange_date() {
        return change_date;
    }
    
    public void setChange_date(ZonedDateTime change_date) {
        this.change_date = change_date;
    }

    public String getChanged_by() {
        return changed_by;
    }
    
    public void setChanged_by(String changed_by) {
        this.changed_by = changed_by;
    }

    public ZonedDateTime getStart_date() {
        return start_date;
    }
    
    public void setStart_date(ZonedDateTime start_date) {
        this.start_date = start_date;
    }

    public ZonedDateTime getEnd_date() {
        return end_date;
    }
    
    public void setEnd_date(ZonedDateTime end_date) {
        this.end_date = end_date;
    }

    public String getAssigned_to() {
        return assigned_to;
    }
    
    public void setAssigned_to(String assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getAssigned_from() {
        return assigned_from;
    }
    
    public void setAssigned_from(String assigned_from) {
        this.assigned_from = assigned_from;
    }

    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }

    public String getCenter_cost() {
        return center_cost;
    }
    
    public void setCenter_cost(String center_cost) {
        this.center_cost = center_cost;
    }

    public ZonedDateTime getRequest_date() {
        return request_date;
    }
    
    public void setRequest_date(ZonedDateTime request_date) {
        this.request_date = request_date;
    }

    public ZonedDateTime getLast_modified_date() {
        return last_modified_date;
    }
    
    public void setLast_modified_date(ZonedDateTime last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public Btr getBtr() {
        return btr;
    }

    public void setBtr(Btr btr) {
        this.btr = btr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Historybtr historybtr = (Historybtr) o;
        if(historybtr.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, historybtr.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Historybtr{" +
            "id=" + id +
            ", btrstatusbefore='" + btrstatusbefore + "'" +
            ", btrstatusafter='" + btrstatusafter + "'" +
            ", change_date='" + change_date + "'" +
            ", changed_by='" + changed_by + "'" +
            ", start_date='" + start_date + "'" +
            ", end_date='" + end_date + "'" +
            ", assigned_to='" + assigned_to + "'" +
            ", assigned_from='" + assigned_from + "'" +
            ", location='" + location + "'" +
            ", center_cost='" + center_cost + "'" +
            ", request_date='" + request_date + "'" +
            ", last_modified_date='" + last_modified_date + "'" +
            '}';
    }
}
