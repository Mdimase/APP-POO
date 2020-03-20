
package Practica4Unnoba.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Practica4Unnoba.Entities.Invite;


public interface InviteRepository extends JpaRepository<Invite, Long> {

	@Query("select count(i) from Invite i where i.user.id = :userId and i.event.id = :eventId")
	public int isInvited(@Param("eventId") long eventId, @Param("userId") long userId);
	
	@Query("select i from Invite i where i.id = :inviteId")
	public Invite getInvite(@Param("inviteId")long inviteId);
	
	@Query("select i from Invite i where i.user.id = :userId")
	public List<Invite> findAllInvitationsByUserId (@Param("userId") long userId); 
	
	@Query("select i from Invite i where i.user.id = :userId and i.event.id = :eventId")
	public Invite findInviteByEventAndUser(@Param("eventId") long eventId, @Param("userId") long userId);
	
	@Query("select i from Invite i inner join Event e on (i.event.id = e.id) where e.owner.id = :ownerId")
	public List<Invite> findInvitationsByOwnerId(@Param("ownerId")long ownerId);
	
	@Query("select i from Invite i inner join Event e on (i.event.id = e.id) where e.owner.id = :ownerId and i.event.id = :eventId")
	public List<Invite> findInvitationsAtEventByOwnerId(@Param("ownerId")long ownerId,@Param("eventId")long eventId);
	
	//Agregado por lepo.
	@Query("select count(i) from Invite i where i.event.id = :eventId")
	public int eventHaveInvitations(@Param("eventId") long eventId);
	
}
