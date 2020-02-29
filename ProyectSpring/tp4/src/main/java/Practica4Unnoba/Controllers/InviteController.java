package Practica4Unnoba.Controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Invite;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.EventService;
import Practica4Unnoba.Services.InviteService;
import Practica4Unnoba.Services.UserService;
@RestController
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
	
	@GetMapping("/addinvite")
	public String showAddInviteForm(Model model) {
		//guardo todos los eventos y usuarios en arreglos
		List<Event> events = new ArrayList<Event>();
		List<Usuario> users = new ArrayList<Usuario>();
		events.addAll(eventService.findAllEvents());
		users.addAll(userService.getAllUsers());
		
		model.addAttribute("users", users);
		model.addAttribute("events", events);
		
		System.out.println(users);
		System.out.println(events);
		
		return "invitations";
	}
	
	@PostMapping("/addinvite")
	public void addInvite (@RequestBody Invite invite) {
		inviteService.addInvite(invite);
	}
	
	
}
