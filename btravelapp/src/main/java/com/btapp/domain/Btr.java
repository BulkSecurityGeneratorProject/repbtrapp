package com.btapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Btr.
 */
@Entity
@Table(name = "btr")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "btr")
public class Btr implements Serializable {

    /**
	 * 
	 */
	//private static final long serialVersionUID = -2819923191665331323L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //@NotNull
    @Column(name = "status", nullable = false)
    private String status;
    
    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime start_date;
    
    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime end_date;
    
    @NotNull
    @Column(name = "location", nullable = false)
    private String location;
    
    @NotNull
    @Column(name = "center_cost", nullable = false)
    private String center_cost;
    
    //@NotNull
    @Column(name = "request_date", nullable = false)
    private ZonedDateTime request_date;
    
    //@NotNull
    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime last_modified_date;
    
    // suma_totala added
    //@NotNull
    @Column(name = "suma_totala", nullable = true)
    private Double suma_totala;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

	@OneToMany(mappedBy = "btr")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Expense> expenses = new HashSet<>();

	@ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private User assigned_to;

    @ManyToOne
    @JoinColumn(name = "assigned_from_id")
    private User assigned_from;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private User supplier;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Btr btr = (Btr) o;
        if(btr.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, btr.id);
    }

    public User getAssigned_from() {
        return assigned_from;
    }

    public User getAssigned_to() {
        return assigned_to;
    }

    public String getCenter_cost() {
        return center_cost;
    }

    public ZonedDateTime getEnd_date() {
        return end_date;
    }
    
    public Set<Expense> getExpenses() {
        return expenses;
    }

    public Long getId() {
        return id;
    }
    
    public ZonedDateTime getLast_modified_date() {
        return last_modified_date;
    }

    public String getLocation() {
        return location;
    }
    
    public User getManager() {
        return manager;
    }

    public ZonedDateTime getRequest_date() {
        return request_date;
    }
    
    public ZonedDateTime getStart_date() {
        return start_date;
    }

    public String getStatus() {
        return status;
    }
    
    public Double getSuma_totala() {
		return suma_totala;
	}

    public User getSupplier() {
        return supplier;
    }
    
    public User getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    
    public void setAssigned_from(User user) {
        this.assigned_from = user;
    }

    public void setAssigned_to(User user) {
        this.assigned_to = user;
    }

    public void setCenter_cost(String center_cost) {
        this.center_cost = center_cost;
    }

    public void setEnd_date(ZonedDateTime end_date) {
        this.end_date = end_date;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLast_modified_date(ZonedDateTime last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setManager(User user) {
        this.manager = user;
    }

    public void setRequest_date(ZonedDateTime request_date) {
        this.request_date = request_date;
    }

    public void setStart_date(ZonedDateTime start_date) {
        this.start_date = start_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSuma_totala(Double suma_totala) {
		this.suma_totala = suma_totala;
	}

    public void setSupplier(User user) {
        this.supplier = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Btr{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", start_date='" + start_date + "'" +
            ", end_date='" + end_date + "'" +
            ", location='" + location + "'" +
            ", center_cost='" + center_cost + "'" +
            ", request_date='" + request_date + "'" +
            ", last_modified_date='" + last_modified_date + "'" +
            ", suma_totala='" + suma_totala + "'" +
            '}';
    }

}
