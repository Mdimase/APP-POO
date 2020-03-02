package Practica4Unnoba.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Invite;
import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Registration;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.EventService;
import Practica4Unnoba.Services.InviteService;
import Practica4Unnoba.Services.RegistrationService;
import Practica4Unnoba.Services.UserService;

@Controller
public class RegistrationController {
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private InviteService inviteService;
	
	@GetMapping("/events/registration/{id}")
	public String showRegistrationForm(@PathVariable long id, Registration registration, Model model) {
		Event event = eventService.getEvent(id);
		
		int numberOfRegistrations = registrationService.quantityOfRegistrationByEvent(id);
		int spaceAvailable = (event.getCapacity() - numberOfRegistrations);
		
		model.addAttribute("spaceAvailable", spaceAvailable);
		model.addAttribute("event", event);
		
		return "registration";
	}
	
	@GetMapping("/myregistrations")
	public String findMyAllRegistrations(Model model) {
		List<Registration> registrations = new ArrayList<Registration>();
		registrations.addAll(registrationService.findAllRegistrationsByUserId(userService.getUserLogged().getId()));
		model.addAttribute("registrations", registrations);
		return "my-registrations";
	}
	
	@PostMapping("/add-registration/{eventId}")
	public String addRegistration(@Valid Registration registration, @PathVariable Long eventId,  BindingResult bindingResult,Model model) {
		String view = "home";
		//obtengo el evento al que se quieren inscribir
		Event event = eventService.getEvent(eventId);
		Usuario userLogged = userService.getUserLogged();
		Date date = new Date();
		
		int numberOfRegistrations = registrationService.quantityOfRegistrationByEvent(eventId);
		int spaceAvailable = (event.getCapacity() - numberOfRegistrations);
		
		registration.setUser(userLogged);
		registration.setEvent(event);
		registration.setCreatedAt(date);
		
		//consigo el wrapper Float para usar el equals
		Float cost = registration.getEvent().getCost();
		
		//se quiere registrar el dueño a su propio evento
		if(registration.getUser().equals(event.getOwner())) {
			String errorOwner = "ERROR:sos el dueño, no hace falta registrarse";
			model.addAttribute("errorOwner", errorOwner);
			model.addAttribute("event", event);
			model.addAttribute("spaceAvailable", spaceAvailable);
			view="registration";
			return view;
		}
		
		//usuario que ya esta inscripto a este evento
		if(registrationService.isRegistered(registration.getEvent().getId(), registration.getUser().getId())) {
			String errorAlreadyRegister = "ERROR:ya estas inscripto a este evento";
			model.addAttribute("errorAlreadyRegister", errorAlreadyRegister);
			model.addAttribute("event", event);
			model.addAttribute("spaceAvailable", spaceAvailable);
			view="registration";
			return view;
		}
		
		//cantidad de cupos disponibles insuficiente
		if(registration.getEvent().getCapacity() <= registrationService.quantityOfRegistrationByEvent(eventId)) {
			String errorNoPlace = "ERROR:no hay lugar disponible";
			model.addAttribute("errorNoPlace", errorNoPlace);
			model.addAttribute("event", event);
			model.addAttribute("spaceAvailable", spaceAvailable);
			view="registration";
			return view;
		}
		
		//fecha incorrecta
		if((registration.getCreatedAt().before(registration.getEvent().getStartRegistrations())) || (registration.getCreatedAt().after(registration.getEvent().getEndRegistrations()))) {
			String errorBadDate = "ERROR:fecha invalida";
			model.addAttribute("errorBadDate", errorBadDate);
			model.addAttribute("event", event);
			model.addAttribute("spaceAvailable", spaceAvailable);
			view="registration";
			return view;
		}
		
		//evento publico
		if(!registration.getEvent().isPrivateEvent()) {
			
			//evento gratis
			if(cost.equals(0.0f)) {
				registrationService.addRegistration(registration);
				return view;
			}
			
			//evento pago
			else {
				Payment payment = new Payment();
				model.addAttribute("payment", payment);
				model.addAttribute("eventId", eventId);
				view = "payment";
				return view;
			}
		}
		
		//evento privado
		else {
			//usuario sin invitacion a este evento
			if(!inviteService.isInvited(eventId,userLogged.getId())){
				String errorNoInvitation = "ERROR:no tienes invitacion a este evento";
				model.addAttribute("errorNoInvitation", errorNoInvitation);
				model.addAttribute("event", event);
				model.addAttribute("spaceAvailable", spaceAvailable);
				view="registration";
				return view;
			}
			//usuario con invitacion a este evento
			else {
				//evento gratis
				if(cost.equals(0.0f)) {
					registrationService.addRegistration(registration);
					return view;
				}
				//evento pago
				else {
					Payment payment = new Payment();
					model.addAttribute("payment", payment);
					model.addAttribute("eventId", eventId);
					view = "payment";
					return view;
				}
			}
		}
	}
}