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
		String message = null;
		String classMessage = null;
		List<Registration> registrations = registrationService.findAllRegistrationsByEventID(eventId);
		
		//si el evento tiene inscripciones
		if(!registrations.isEmpty()) {
			message = "<b>Error:</b> no se puede eliminar este evento ya que tiene usuarios registrados.";
			classMessage = "alert-danger";
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("classMessage", classMessage);
			return "redirect:/events/info/"+eventId;
		}
		
		//borro las invitaciones enviadas a este evento por el usuario logueado, para no violar FK en BD
		inviteService.deleteInvitationsSent(eventId);
		eventService.deleteEvent(eventId);
		return "redirect:/events/my";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditEventForm(@PathVariable Long id, Model model) {
		model.addAttribute("event", eventService.getEvent(id));
		return "edit";
	}
	
	@PostMapping("/edit/{eventId}")	
	public String editEvent(@Valid Event event, BindingResult result, @PathVariable Long eventId, Model model, RedirectAttributes redirectAttributes) {
		//error de validacion de formulario
		if (result.hasErrors()) {
			return "edit";
	    }
		String classMessage = null;
		
		//seteo el id al evento para poder buscarlo en BD
		event.setId(eventId);
		
		String message = eventService.validateEdit(event);
		if(message != null) {
			classMessage = "alert-danger";
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("classMessage", classMessage);
			return "redirect:/events/edit/" + eventId;
		}
		
		//si esta todo bien edito
		eventService.updateEvent(event , eventId);
		
		message = "Se editó correctamente el evento.";
		classMessage = "alert-success";
		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("classMessage", classMessage);
		
		return "redirect:/events/info/" + eventId;
	}	
}
