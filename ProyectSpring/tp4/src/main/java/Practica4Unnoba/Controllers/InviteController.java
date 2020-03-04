package Practica4Unnoba.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

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
@RequestMapping("/invitations")
public class InviteController {
	
	@Autowired
	private InviteService inviteService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserService userService;

	@PostMapping("/delete/{eventId}")
	public String deleteInvite(@PathVariable Long eventId,Model model) {
		Usuario userLogged = userService.getUserLogged();
		Invite invite = inviteService.findInviteByEventAndUser(eventId,userLogged.getId());
		inviteService.deleteInvite(invite.getId());
		return "redirect:/invitations/my";	//importante poner las barras xq es una ruta no una view
	}	
	
	@PostMapping("delete/invitesent/{inviteId}")
	public String deleteInviteSent(@PathVariable Long inviteId,Model model) {
		Long eventId = inviteService.getInvite(inviteId).getEvent().getId();
		inviteService.deleteInvite(inviteId);
		return "redirect:/events/info/"+eventId;
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
		//guardo todos los usuarios menos el owner de este evento en arreglo
		List<Usuario> users = new ArrayList<Usuario>();
		users.addAll(userService.getAllUsers());
		users.remove(userService.getUserLogged());
		Event event = eventService.getEvent(eventId);
		model.addAttribute("users", users);
		model.addAttribute("event", event);
		return "invitations";
	}
	
	@PostMapping("/add/{eventId}")
	public String addInvite (String username,@PathVariable Long eventId,Model model) {
		String view  = "home";
		Usuario user = userService.findUserByUsername(username);
		Event event = eventService.getEvent(eventId);
		
		//ya esta invitado
		if(inviteService.isInvited(eventId, user.getId())) {
			String errorAlreadyInvited = "ERROR: Ya tiene una invitacion este usuario";
			List<Usuario> users = new ArrayList<Usuario>();
			users.addAll(userService.getAllUsers());
			users.remove(userService.getUserLogged());
			model.addAttribute("users", users);
			model.addAttribute("event", event);
			model.addAttribute("errorAlreadyInvited", errorAlreadyInvited);
			view="invitations";
			return view;
		}
		else {
			Invite invite = new Invite(user,event);
			inviteService.addInvite(invite);
			return "redirect:/events/info/"+eventId;
		}
	}
}
