package Practica4Unnoba.Entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Registration registration;
	
	@NotBlank(message="card is required")
	private String card;
	
	@NotBlank(message="cardNumber is required")
	private String cardNumber;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Registration getRegistration() {
		return registration;
	}
	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	
}