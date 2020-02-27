package Practica4Unnoba.Controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Registration;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.EventService;
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
	
	@GetMapping("/events/registration/{id}")
	public String showRegistrationForm(@PathVariable long id, Registration registration, Model model) {
		Event event = eventService.getEvent(id);
		
		int numberOfRegistrations = registrationService.quantityOfRegistrationByEvent(id);
		int spaceAvailable = (event.getCapacity() - numberOfRegistrations);
		
		model.addAttribute("spaceAvailable", spaceAvailable);
		model.addAttribute("event", event);
		
		return "registration";
	}
	
	@PostMapping("/add-registration/{id}")
	public String addRegistration(@Valid Registration registration, @PathVariable Long id,  BindingResult bindingResult,Model model) {
		String view = "home";
		//obtengo el evento al que se quieren inscribir
		Event event = eventService.getEvent(id);
		Usuario user = userService.getUserLogged();
		Date date = new Date();
		
		int numberOfRegistrations = registrationService.quantityOfRegistrationByEvent(id);
		int spaceAvailable = (event.getCapacity() - numberOfRegistrations);
		
		registration.setUser(user);
		registration.setEvent(event);
		registration.setCreatedAt(date);
		
		System.out.println(registrationService.isRegistered(registration.getEvent().getId(), user.getId()));
		System.out.println(registrationService.isRegistered(registration.getEvent().getId(), registration.getUser().getId()));
		
		//se quiere registrar el dueño a su propio evento
		if(registration.getUser().equals(event.getOwner())) {
			Boolean errorOwner = new Boolean(true);
			model.addAttribute("errorOwner", errorOwner);
			model.addAttribute("event", event);
			model.addAttribute("spaceAvailable", spaceAvailable);
			view="registration";
			return view;
		}
		
		//usuario que ya esta inscripto a este evento
		if(registrationService.isRegistered(registration.getEvent().getId(), registration.getUser().getId())) {
			Boolean errorAlreadyRegister = new Boolean(true);
			model.addAttribute("errorAlreadyRegister", errorAlreadyRegister);
			model.addAttribute("event", event);
			model.addAttribute("spaceAvailable", spaceAvailable);
			view="registration";
			return view;
		}
		
		//cantidad de cupos disponibles insuficiente
		if(registration.getEvent().getCapacity() <= registrationService.quantityOfRegistrationByEvent(id)) {
			Boolean errorNoPlace = new Boolean(true);
			model.addAttribute("errorNoPlace", errorNoPlace);
			model.addAttribute("event", event);
			model.addAttribute("spaceAvailable", spaceAvailable);
			view="registration";
			return view;
		}
		
		//fecha incorrecta
		if((registration.getCreatedAt().before(registration.getEvent().getStartRegistrations())) || (registration.getCreatedAt().after(registration.getEvent().getEndRegistrations()))) {
			Boolean errorBadDate = new Boolean(true);
			model.addAttribute("errorBadDate", errorBadDate);
			model.addAttribute("event", event);
			model.addAttribute("spaceAvailable", spaceAvailable);
			view="registration";
			return view;
		}
		
		
		//evento publico
		if(!registration.getEvent().isPrivateEvent()) {
			Float cost = registration.getEvent().getCost();
			//evento gratis
			if(cost.equals(0.0f)) {
				registrationService.addRegistration(registration);
				return view;
			}
			//evento pago
			else {
				
			}
		}
		//evento privado
		else {
			
		}
		
		/*
		
		//evento publico
		if(!registration.getEvent().isPrivateEvent()) {
			//espacio disponible
			if(registration.getEvent().getCapacity() > registrationService.quantityOfRegistrationByEvent(id)) {
				//usuario no registrado en el evento
				if(!registrationService.isRegistered(registration.getEvent().getId(), registration.getUser().getId())) {
					//fecha correcta
					if((registration.getCreatedAt().after(registration.getEvent().getStartRegistrations())) && (registration.getCreatedAt().before(registration.getEvent().getEndRegistrations()))) {
						registrationService.addRegistration(registration);
					}
				}
			}
		}
		
		//evento privado
		else {
			//usuario no registrado en el evento
			if(!registrationService.isRegistered(registration.getEvent().getId(), registration.getUser().getId())) {
				//no tiene pago
				if(!registrationService.isPayment(registration)) {
					if(registration.getEvent().getCapacity() > registrationService.quantityOfRegistrationByEvent(registration.getEvent().getId())) {
						//hay lugares libres
						if(registration.getCreatedAt().after(registration.getEvent().getStartRegistrations()) && registration.getCreatedAt().before(registration.getEvent().getEndRegistrations())) {
						//fecha correcta
							//registrationService.addRegistration(registration);
							System.out.println("No tiene pago " + registration.getId());
							Payment p = new Payment();
							p.setEvent(id);
							model.addAttribute("payment", p);
							view = "payment";
							//return "home";
						}
					}
				}

			}
		}
		
		*/
		
		return view;
		
	}
	
	@PostMapping("/registrations")
	public void addRegistration(@RequestBody Registration registration) {
		Usuario u = userService.getUserById(registration.getUser().getId());
		registration.setUser(u);
		Event e = eventService.getEvent(registration.getEvent().getId());
		registration.setEvent(e);
		registrationService.addRegistration(registration);
	}
	
}