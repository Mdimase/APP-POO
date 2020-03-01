
package Practica4Unnoba.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Practica4Unnoba.Entities.Invite;


public interface InviteRepository extends JpaRepository<Invite, Long> {

	@Query("select count(i) from Invite i where i.user.id = :userId and i.event.id = :eventId")
	public int isInvited(@Param("eventId") long eventId, @Param("userId") long userId);
	
	@Query("select i from Invite i where i.user.id = :userId")
	public List<Invite> findAllInvitationsByUserId (@Param("userId") long userId); 
	
	@Query("select i from Invite i where i.user.id = :userId and i.event.id = :eventId")
	public Invite findInviteByEventAndUser(@Param("eventId") long eventId, @Param("userId") long userId);
}
