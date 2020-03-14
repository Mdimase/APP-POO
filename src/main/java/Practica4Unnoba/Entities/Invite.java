package Practica4Unnoba.Entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;

@Entity
public class Invite {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	//Agregado por lepo
	//@OnDelete(action=OnDeleteAction.CASCADE)
	private Usuario user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	//Agregado por lepo
	//@OnDelete(action=OnDeleteAction.CASCADE)
	private Event event;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public Invite() {
		super();
	}
	
	public Invite(Usuario user, Event event) {
		super();
		this.user = user;
		this.event = event;
	}
	
}
