package Practica4Unnoba.Services;

import Practica4Unnoba.Repositories.RegistrationRepository;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Registration;
import Practica4Unnoba.Entities.Usuario;

@Service
public class RegistrationService {
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private InviteService inviteService;

	@Autowired
	private UserService userService;
	
	public List<Registration> getAllRegistration (){
		return registrationRepository.findAll();
	}

	
	public List<Registration> findAllRegistrationsByUserId(Long userId){
		return registrationRepository.findAllByUserId(userId);
	}
	
	
	public List<Registration> findAllRegistrationsByEventID(Long event_id) {
		List<Registration> registrations = registrationRepository.findAllByEventID(event_id);
		return registrations;
	}
	
	public void addRegistration(Registration registration) {
		registrationRepository.save(registration);
	}
	
	public Registration getRegistration(Long id) {
	    return registrationRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
	}
	
	public int quantityOfRegistrationByEvent(Long event_id) {
		return registrationRepository.quantityOfRegistrationByEvent(event_id);
	}
	
	public boolean isRegistered(Long event_id, Long user_id) {
		int registration = registrationRepository.isRegistered(event_id, user_id);
		if(registration>0) {
			return true;
		}
		return false;
	}
	
	//devuelve true si el evento pasado por parametro tiene alguna inscripción
	public boolean eventHaveRegistration (Long eventId) {
		if(registrationRepository.eventHaveRegistration(eventId) > 0) {
			return true;
		}
		return false;
	}
	
	//retorna mensaje de error si alguna validación da error, sino null
	public String validate(Registration registration, Event event) {
		if(this.validateOwner(registration, event)!= null) {
			return this.validateOwner(registration, event);
		}
		if(this.validateAlreadyRegister(registration)!=null) {
			return this.validateAlreadyRegister(registration);
		}
		if(this.validateDate(registration)!= null) {
			return this.validateDate(registration);
		}
		if(this.validateSpace(registration)!= null) {
			return this.validateSpace(registration);
		}
		if(event.isPrivateEvent()) {
			if(this.validateHaveInvitation(registration) != null) {
				return this.validateHaveInvitation(registration);
			}
		}
		return null;
	}
	
	public String validateOwner(Registration registration, Event event) {
		String message = null;
		if(registration.getUser().equals(event.getOwner())) {
			message = "<b>Error:</b> no puedes registrarte al evento ya que eres el dueño.";
		}
		return message;
	}
	
	public String validateAlreadyRegister(Registration registration) {
		String message = null;
		if(isRegistered(registration.getEvent().getId(), registration.getUser().getId())) {
			message = "<b>Error:</b> ya estás registrado en el evento.";
		}
		return message;
	}
	
	public String validateSpace(Registration registration) {
		String message = null;
		if(registration.getEvent().getCapacity() <= this.quantityOfRegistrationByEvent(registration.getEvent().getId())) {
			message = "<b>Error:</b> no hay lugar disponible.";
		}
		return message;
	}
	
	public String validateDate(Registration registration) {
		String message = null;
		if((registration.getCreatedAt().before(registration.getEvent().getStartRegistrations())) || (registration.getCreatedAt().after(registration.getEvent().getEndRegistrations()))) {
			message = "<b>Error:</b> fecha invalida.";
		}
		return message;
	}
	
	public String validateHaveInvitation(Registration registration) {
		String message = null;
		Usuario userLogged = userService.getUserLogged();
		if(!inviteService.isInvited(registration.getEvent().getId(),userLogged.getId())) {
			message = "<b>Error:</b> no tienes invitación a este evento.";
		}
		return message;
	}
	
	public boolean isPayment(Registration registration) {
		for(Payment p : paymentService.findAllPayments()){
			Long reg = registration.getId();
			Long pReg = p.getRegistration().getId();
			if(reg.equals(pReg)) {
				return true;
			}
		}
		return false;
	}
}