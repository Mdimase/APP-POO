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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Invite;
import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Registration;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.EventService;
import Practica4Unnoba.Services.InviteService;
import Practica4Unnoba.Services.PaymentService;
import Practica4Unnoba.Services.RegistrationService;
import Practica4Unnoba.Services.UserService;

@Controller
@RequestMapping("/events")
public class EventController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private InviteService inviteService;
	
	@GetMapping("/my")
	public String findAllMyEvents(Model model) {
		//obtengo el usuario logueado
		Usuario user = userService.getUserLogged();
		
		List<Event> events = eventService.findAllEventByOwnerIdOrderByDate(user.getId());
		List<Integer> spacesAvailables = eventService.getSpacesAvailables(events);
		
		model.addAttribute("events", events);
		model.addAttribute("spacesAvailables",spacesAvailables);
		
		return "my-events";
	}
	
	@GetMapping("/add")
	public String showAddEventForm(Event event) {
		return "add-event";
	}
	
	
	@GetMapping("/")
	public String findAllEventsOrderByDate(Model model){
		List<Event> events = eventService.findAllEventsOrderByDate();
		List<Integer> spacesAvailables = eventService.getSpacesAvailables(events);
		
		model.addAttribute("events", events);
		model.addAttribute("spacesAvailables",spacesAvailables);
		
		return "events";
	}
	
	@GetMapping("/info/{id}")
	public String getEvent(@PathVariable Long id, Model model) {
		Event event = eventService.getEvent(id);
		Usuario userLogged = userService.getUserLogged();
		int spaceAvailable = eventService.spaceAvailable(event);
		
		//obtengo las registraciones de este evento
		List<Registration> registrations = registrationService.findAllRegistrationsByEventID(id);
		
		//busqueda de pagos
		List<Payment> payments = paymentService.getPaymentsByRegistrations(registrations);
		
		//consigo las invitaciones enviadas 
		List<Invite> invitationsSent = inviteService.findInvitationsAtEventSentByOwner(userLogged.getId(),id);
		
		//consigo los usuarios invitados a este evento
		List<Usuario> usersInvitated = userService.getUsersInvitated(invitationsSent);
		
		model.addAttribute("event", event);
		model.addAttribute("registrations", registrations);
		model.addAttribute("spaceAvailable", spaceAvailable);
		model.addAttribute("payments", payments);
		model.addAttribute("usersInvitated", usersInvitated);
		model.addAttribute("invitationsSent", invitationsSent);
		
		return "info";
	}
	
	@PostMapping("/add")
	public String addEvent(@Valid Event event, BindingResult result, Model model) {
		//errores de validacion de formulario
		if (result.hasErrors()) {
			return "add-event";
	    }
		
		//obtengo el usuario logueado
		Usuario user = userService.getUserLogged();
		
		event.setOwner(user);
		
		eventService.addEvent(event);
		
		return "redirect:/events/my";
	}
	
	@PostMapping("/delete/{eventId}")
	public String deleteEvent(@PathVariable Long eventId, Model model, RedirectAttributes redirectAttributes) {
		String view = "home";
		
		//Agregado por lepo.
		String message = null;
		String classMessage = null;
		
		Usuario user = userService.getUserLogged();
		
		List<Registration> registrations = new ArrayList<Registration>();
		
		registrations.addAll(registrationService.findAllRegistrationsByEventID(eventId));
		
		//si el evento tiene inscripciones
		if(!registrations.isEmpty()) {
			//String errorHaveRegistrations = "<b>Error:</b> no se puede eliminar este evento ya que tiene usuarios registrados.";
			
			//Agregado por lepo.
			message = "<b>Error:</b> no se puede eliminar este evento ya que tiene usuarios registrados.";
			classMessage = "alert-danger";
			
			Event event = eventService.getEvent(eventId);
			
			//calculo los espacios disponibles
			int numberOfRegistrations = registrationService.quantityOfRegistrationByEvent(eventId);
			int spaceAvailable = (event.getCapacity() - numberOfRegistrations);
			
			//consigo los payments
			List<Payment> payments = new ArrayList<Payment>();
			
			for(Registration registration : registrations) {
				if(registration.getEvent().getCost() > 0.0f) {
					Payment payment = paymentService.getPaymentByRegistration(registration.getId());
					payments.add(payment);
				}
			}
			//consigo las invitaciones enviadas y los datos de esos eventos para mostrarlos
			List<Invite> invitationsSent = new ArrayList<Invite>();
			List<Usuario> usersInvitated = new ArrayList<Usuario>();
			
			invitationsSent.addAll(inviteService.findInvitationsAtEventSentByOwner(user.getId(),event.getId()));
			
			for(Invite i : invitationsSent) {
				usersInvitated.add(userService.getUserById(i.getUser().getId()));
			}
			
			model.addAttribute("payments",payments);
			model.addAttribute("spaceAvailable", spaceAvailable);
			model.addAttribute("registrations",registrations);
			model.addAttribute("event", event);
			//model.addAttribute("errorHaveRegistrations", errorHaveRegistrations);
			model.addAttribute("usersInvitated", usersInvitated);
			model.addAttribute("invitationsSent", invitationsSent);
			
			//Agregado por lepo.
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("classMessage", classMessage);
			
			view = "info";
			
			return view;
		}
		//no tiene registros
		else {
			eventService.deleteEvent(eventId);
			
			return "redirect:/events/my";
		}
	}
	
	@GetMapping("/edit/{id}")
	public String showEditEventForm(@PathVariable Long id, Model model) {
		model.addAttribute("event", eventService.getEvent(id));
		return "edit";
	}
	
	@PostMapping("/edit/{eventId}")	
	public String editEvent(@Valid Event event, BindingResult result, @PathVariable Long Id, Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "edit";
	    }
		
		//Agregado por lepo.
		String message = null;
		String classMessage = null;
		
		//evento en BD, datos todavía sin actualizar
		Event eventBD = eventService.getEvent(Id);
		
		//wrapper costo del evento nuevo a guardar, proveniente de la vista
		Float cost = event.getCost();
		
		//wrapper capacidad del evento nuevo a guardar, proveniente de la vista
		Integer capacity = event.getCapacity();
		
		//si quiero modificar el costo
		if(!cost.equals(eventBD.getCost())) {
			//si tiene registros no lo dejo editar el costo
			if(eventService.haveRegistration(Id)) {
				//String errorHaveRegistrations = "<b>Error:</b> no se puede editar el costo debido a que ya hay usuarios registrados.";
				
				//Agregado por lepo.
				message = "<b>Error:</b> no se puede editar el costo debido a que ya hay usuarios registrados.";
				classMessage = "alert-danger";
				
				//model.addAttribute("errorHaveRegistrations", errorHaveRegistrations);
				model.addAttribute("event", eventBD);
				
				/*Agregado por lepo.
				redirectAttributes.addFlashAttribute("message", message);
				redirectAttributes.addFlashAttribute("classMessage", classMessage);
				*/
				model.addAttribute("message", message);
				model.addAttribute("classMessage", classMessage);
				
				return "edit";
			}
		}
		
		//si quiero modificar la capacidad
		if(!capacity.equals(eventBD.getCapacity())) {
			//si la capacidad que quieren ingresar es menor que la cantidad de registraciones no lo dejo
			if(capacity < registrationService.quantityOfRegistrationByEvent(Id)) {
				//String errorLowCapacity = "<b>Error:</b> no se puede editar la capacidad del evento a un número menor a la cantidad de usuarios ya registrados.";
				
				//Agregado por lepo.
				message = "<b>Error:</b> no se puede editar la capacidad del evento a un número menor a la cantidad de usuarios ya registrados.";
				classMessage = "alert-danger";
				
				//model.addAttribute("errorLowCapacity", errorLowCapacity);
				model.addAttribute("event", eventBD);
				
				/*Agregado por lepo.
				redirectAttributes.addFlashAttribute("message", message);
				redirectAttributes.addFlashAttribute("classMessage", classMessage);
				*/
				model.addAttribute("message", message);
				model.addAttribute("classMessage", classMessage);
				
				return "edit";
				
				//return "redirect:/events/edit/" + eventId;
			}
		}
		
		//si esta todo bien edito
		eventService.updateEvent(event , Id);
		
		//Agregado por lepo.
		message = "Se editó correctamente el evento.";
		classMessage = "alert-success";
		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("classMessage", classMessage);
		
		return "redirect:/events/info/" + Id;
		
	}
		
}
