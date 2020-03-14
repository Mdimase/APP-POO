package Practica4Unnoba.Services;

import Practica4Unnoba.Repositories.InviteRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Service;
import Practica4Unnoba.Entities.Invite;


@Service
public class InviteService {
	
	@Autowired
	private InviteRepository inviteRepository;
	
	@Autowired
	private UserService userService;
	
	public void addInvite(Invite invite) {
	    inviteRepository.save(invite);
	}
	
	public Invite getInvite(Long id) {
		return inviteRepository.getInvite(id);
	}
	    
	public void deleteInvite(@PathVariable Long id) {
		inviteRepository.deleteById(id);
	}
	
	//borra todas las invitaciones enviadas por el usuario de este evento
	public void deleteInvitationsSent(Long eventId) {
		List<Invite> invitationsSent = this.findInvitationsAtEventSentByOwner(userService.getUserLogged().getId(), eventId);
		for(Invite i : invitationsSent) {
			this.deleteInvite(i.getId());
		}
	}
	
	public Invite findInviteByEventAndUser(Long eventId,Long userId) {
		return inviteRepository.findInviteByEventAndUser(eventId, userId);
	}
	
	public List<Invite> findInvitationsSentByOwner(Long ownerId){
		return inviteRepository.findInvitationsByOwnerId(ownerId);
	}
	
	public List<Invite> findInvitationsAtEventSentByOwner(Long ownerId,Long eventId){
		return inviteRepository.findInvitationsAtEventByOwnerId(ownerId, eventId);
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
