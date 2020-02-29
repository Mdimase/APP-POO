package Practica4Unnoba.Controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Invite;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.EventService;
import Practica4Unnoba.Services.InviteService;
import Practica4Unnoba.Services.UserService;

@Controller
public class InviteController {
	
	@Autowired
	private InviteService inviteService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserService userService;

	@DeleteMapping("/invite/{id}")
	public void deleteInvite(@PathVariable Long id) {
		inviteService.deleteInvite(id);
	}	
	
	@GetMapping("/addinvite/{eventId}")
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
	
	@PostMapping("/addinvite")
	public String addInvite (Model model) {
		System.out.println("post controller");
		System.out.println(model.asMap());
		//inviteService.addInvite(invite);
		return "home";
	}
	
	
}
