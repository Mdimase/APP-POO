package Practica4Unnoba.Controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Registration;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.EventService;
import Practica4Unnoba.Services.PaymentService;
import Practica4Unnoba.Services.RegistrationService;
import Practica4Unnoba.Services.UserService;

@Controller
public class EventController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/myevents")
	public String findAllMyEvents(Model model) {
		//obtengo el usuario logueado
		Usuario user = userService.getUserLogged();
		
		List<Event> events = eventService.findAllEventByOwnerIdOrderByDate(user.getId());
		List<Integer> spacesAvailables = eventService.getSpacesAvailables(events);
		
		model.addAttribute("events", events);
		model.addAttribute("spacesAvailables",spacesAvailables);
		
		return "my-events";
	}
	
	@GetMapping("/addevent")
	public String showAddEventForm(Event event) {
		return "add-event";
	}
	
	
	@GetMapping("/events/")
	public String findAllEventsOrderByDate(Model model){
		List<Event> events = eventService.findAllEventsOrderByDate();
		List<Integer> spacesAvailables = eventService.getSpacesAvailables(events);
		
		model.addAttribute("events", events);
		model.addAttribute("spacesAvailables",spacesAvailables);
		
		return "events";
	}
	
	@GetMapping("/events/info/{id}")
	public String getEvent(@PathVariable Long id, Model model) {
		Event event = eventService.getEvent(id);
		List<Registration> registrations = registrationService.findAllRegistrationsByEventID(id);
		
		//calculo los espacios disponibles
		int numberOfRegistrations = registrationService.quantityOfRegistrationByEvent(id);
		int spaceAvailable = (event.getCapacity() - numberOfRegistrations);
		
		//busqueda de pagos
		//busco en cada registracion de pago y guardo dicho pago en un array
		List<Payment> payments = new ArrayList<Payment>();
		for(Registration registration : registrations) {
			if(registration.getEvent().getCost() > 0.0f) {
				Payment payment = paymentService.getPaymentByRegistration(registration.getId());
				payments.add(payment);
			}
		}
		
		model.addAttribute("event", event);
		model.addAttribute("registrations", registrations);
		model.addAttribute("spaceAvailable", spaceAvailable);
		model.addAttribute("payments", payments);
		
		return "info";
	}
	
	@GetMapping("/events/edit/{id}")
	public String editEvent(@PathVariable Long id, Model model) {
		model.addAttribute("event", eventService.getEvent(id));
		
		return "edit";
	}
	
	@PostMapping("/addevent/")
	public String addEvent(@Valid Event event, BindingResult result, Model model) {
		//errores de validacion de formulario
		if (result.hasErrors()) {
			return "add-event";
	    }
		//obtengo el usuario logueado
		Usuario user = userService.getUserLogged();
		
		event.setOwner(user);
		eventService.addEvent(event);
		
		return "home";
	}
	
	@PostMapping("/events/{id}")
	public String deleteEvent(@PathVariable Long id, Model model) {
		//obtengo el usuario logueado
		Usuario user = userService.getUserLogged();
		
		if(eventService.getEvent(id).getOwner().getId() == user.getId()) {
				eventService.deleteEvent(id);
				
				List<Event> events = eventService.findAllEventByOwnerIdOrderByDate(user.getId());
				model.addAttribute("events", events);
				
				return "my-events";
		}
		
		List<Event> events = eventService.findAllEvents();
		model.addAttribute("events", events);
		
		return "events";
	}
	
	@GetMapping("/events/{id}")	
	public String replaceEvent(@Valid Event event, BindingResult result, @PathVariable Long id, Model model) {
		if (result.hasErrors()) {
			return "edit";
	    }
		//obtengo el usuario logueado
		Usuario user = userService.getUserLogged();
		
		if(eventService.getEvent(id).getOwner().getId() == user.getId()) {
			eventService.updateEvent(event , id);
			
			List<Event> events = eventService.findAllEventByOwnerIdOrderByDate(user.getId());
			model.addAttribute("events", events);
			
			return "my-events";
		}
		
		List<Event> events = eventService.findAllEvents();
		model.addAttribute("events", events);
		
		return "events";
	}
		
}
