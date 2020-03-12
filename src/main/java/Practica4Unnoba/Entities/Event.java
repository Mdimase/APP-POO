package Practica4Unnoba.Entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	//Agregado por lepo
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Usuario owner;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date eventDate;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startRegistrations;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endRegistrations;
	
	@NotBlank(message = "Name is required")
	private String name;
	
	@NotBlank(message = "Place is required")
	private String place;
	
	//@NotBlank(message = "Cost is required")
	//Descomentado por lepo
	@Min(value=1, message="Min capacity 1")
	private int capacity;
	
	//@NotBlank(message = "Cost is required")
	//@Min(value=0,message="costo minimo 0")
	private float cost;
	
	private boolean privateEvent;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Usuario getOwner() {
		return owner;
	}
	public void setOwner(Usuario owner) {
		this.owner = owner;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public Date getStartRegistrations() {
		return startRegistrations;
	}
	public void setStartRegistrations(Date startRegistrations) {
		this.startRegistrations = startRegistrations;
	}
	public Date getEndRegistrations() {
		return endRegistrations;
	}
	public void setEndRegistrations(Date endRegistrations) {
		this.endRegistrations = endRegistrations;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	public boolean isPrivateEvent() {
		return privateEvent;
	}
	public void setPrivateEvent(boolean privateEvent) {
		this.privateEvent = privateEvent;
	}
	
	@Override
	public String toString() {
		return "Event [id=" + id + ", owner=" + owner + ", eventDate=" + eventDate + ", startRegistrations="
				+ startRegistrations + ", endRegistrations=" + endRegistrations + ", name=" + name + ", place=" + place + ", capacity=" + capacity + ", cost="
				+ cost + ", privateEvent=" + privateEvent + "]";
	}
	
}