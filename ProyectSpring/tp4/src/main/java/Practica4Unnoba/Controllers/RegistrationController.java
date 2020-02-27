package Practica4Unnoba.Controllers;

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
		
		/*
		System.out.println("Capacity: " + event.getCapacity());
		System.out.println("Num. of registrations: " + numberOfRegistrations);
		System.out.println("Space available: " + spaceAvailable);
		*/
		
		model.addAttribute("numberOfRegistrations", spaceAvailable);
		model.addAttribute("event", event);
		
		return "registration";
	}
	
	@PostMapping("/add-registration/{id}")
	public String addRegistration(@Valid Registration registration, @PathVariable Long id,  BindingResult bindingResult,Model model) {
		//obtengo el usuario logueado
		Usuario user = userService.getUserLogged();
		
		registration.setUser(user);
		
		Event event = eventService.getEvent(id);
		registration.setEvent(event);
		
		Date date = new Date();
		registration.setCreatedAt(date);
		
		/*
		System.out.println("Event date: " + e.getEventDate() + ", Start registrations: " + e.getStartRegistrations() + ", End registrations: " + e.getEndRegistrations());
		System.out.println("Created at: " + date);
		*/
		
		String view = "home";
		
		//evento publico
		if(!registration.getEvent().isPrivateEvent()) {
			if(registration.getEvent().getCapacity() > registrationService.quantityOfRegistrationByEvent(id)) {
				System.out.println(registration.getEvent().getId()+"\n"+registration.getUser());
				//usuario no registrado en el evento
				if(!registrationService.isRegistered(registration.getEvent().getId(), registration.getUser().getId())) {
					//hay lugares libres
					if((registration.getCreatedAt().after(registration.getEvent().getStartRegistrations())) && (registration.getCreatedAt().before(registration.getEvent().getEndRegistrations()))) {
						//fecha correcta
						registrationService.addRegistration(registration);
						
						//return "home";
					}
				}
			}
		}
		//evento privado
		else {
			//usuario no registrado en el evento
			if(!registrationService.isRegistered(registration.getEvent().getId(), registration.getUser().getId())) {
				if(!registrationService.isPayment(registration)) {
					//no tiene pago
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