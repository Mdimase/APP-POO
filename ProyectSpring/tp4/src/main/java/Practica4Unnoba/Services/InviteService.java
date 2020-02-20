/*
package Practica4Unnoba.Services;

import java.util.List;

import Practica4Unnoba.Repositories.InviteRepository;
import Practica4Unnoba.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Service;
import Practica4Unnoba.Entities.Invite;


@Service
public class InviteService {
	
	@Autowired
	private InviteRepository inviteRepository;
	
	public void addInvite(Invite invite) {
	    inviteRepository.save(invite);
	}
	    
	public void deleteInvite(@PathVariable Long id) {
		inviteRepository.deleteById(id);
	}
}
*/