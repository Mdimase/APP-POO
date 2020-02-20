/*
package Practica4Unnoba.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import Practica4Unnoba.Entidades.Invite;
import Practica4Unnoba.Services.InviteService;
@RestController
public class InviteController {
	
	@Autowired
	private InviteService inviteServicios;

	@DeleteMapping("/invites/{id}")
	void deleteInvite(@PathVariable Long id) {
		inviteServicios.deleteInvite(id);
	}	
	@PostMapping("/invites")
	public void addInvite (@RequestBody Invite invite) {
		inviteServicios.addInvite(invite);
	}
	
	
}
*/