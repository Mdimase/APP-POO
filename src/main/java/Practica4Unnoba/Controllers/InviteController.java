package Practica4Unnoba.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Invite;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.EventService;
import Practica4Unnoba.Services.InviteService;
import Practica4Unnoba.Services.UserService;

@Controller
@RequestMapping("/invitations")
public class InviteController {
	
	@Autowired
	private InviteService inviteService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserService userService;

	//borrar invitaciones recibidas
	@PostMapping("/delete/{eventId}")
	public String deleteInvite(@PathVariable Long eventId, Model model) {
		Usuario userLogged = userService.getUserLogged();
		Invite invite = inviteService.findInviteByEventAndUser(eventId,userLogged.getId());
		inviteService.deleteInvite(invite.getId());
		return "redirect:/invitations/my";	//importante poner las barras porque es una ruta no una view
	}	
	
	//borrar invitaciones enviadas
	@PostMapping("delete/invitesent/{inviteId}")
	public String deleteInviteSent(@PathVariable Long inviteId, Model model, RedirectAttributes redirectAttributes) {
		//Agregado por lepo.
		String message = null;
		String classMessage = null;
		
		Long eventId = inviteService.getInvite(inviteId).getEvent().getId();
		inviteService.deleteInvite(inviteId);
		
		//Agregado por lepo.
		message = "Invitación eliminada correctamente.";
		classMessage = "alert-success";
		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("classMessage", classMessage);
		return "redirect:/events/info/" + eventId;
	}
	
	@GetMapping("/my")
	public String findAllMyInvitations(Model model) {
		Usuario userLogged = userService.getUserLogged();
		
		//consigo los eventos a los que esta invitado este usuario
		List<Event> events = new ArrayList<Event>();
		events.addAll(eventService.findAllEventWithInvitationsByUserId(userLogged.getId()));
		model.addAttribute("events", events);
		return "my-invitations";
	}
	
	@GetMapping("/add/{eventId}")
	public String showAddInviteForm(Model model,@PathVariable Long eventId) {
		Event event = eventService.getEvent(eventId);
		//guardo todos los usuarios menos el owner de este evento en arreglo
		List<Usuario> users = new ArrayList<Usuario>();
		users.addAll(userService.getAllUsers());
		users.remove(userService.getUserLogged());

		model.addAttribute("users", users);
		model.addAttribute("event", event);
		return "invitations";
	}
	
	@PostMapping("/add/{eventId}")
	public String addInvite (String username, @PathVariable Long eventId, Model model, RedirectAttributes redirectAttributes) {
		String message = null;
		String classMessage = null;
		Event event = eventService.getEvent(eventId);
		
		if(username.equals("- Select -")) {
			return "redirect:/invitations/add/" + eventId;
		}
		
		Usuario user = userService.findUserByUsername(username);
		
		//ya esta invitado
		if(inviteService.isInvited(eventId, user.getId())) {
			message = "<b>Error:</b> este usuario ya tiene una invitación al evento.";
			classMessage = "alert-danger";
			redirectAttributes.addFlashAttribute("message", message);
			redirectAttributes.addFlashAttribute("classMessage", classMessage);
			
			return "redirect:/invitations/add/" + eventId;
		}
		
		Invite invite = new Invite(user,event);
		inviteService.addInvite(invite);

		message = "Invitación enviada correctamente."; 
		classMessage = "alert-success";
		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("classMessage", classMessage);
		
		return "redirect:/events/info/" + eventId;
		}
	}

