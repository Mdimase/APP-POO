package Practica4Unnoba.Services;

import Practica4Unnoba.Repositories.InviteRepository;

import java.util.List;

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
	
	public Invite findInviteByEventAndUser(Long eventId,Long userId) {
		return inviteRepository.findInviteByEventAndUser(eventId, userId);
	}
	
	public List<Invite> getAllInvitations() {
		return inviteRepository.findAll();
	}
	
	public List<Invite> findAllInvitationsByUserId(Long userId){
		return inviteRepository.findAllInvitationsByUserId(userId);
	}
	
	public boolean isInvited(Long eventId,Long userId) {
		if(inviteRepository.isInvited(eventId, userId) > 0) {
			return true;
		}
		return false;
	}
}
