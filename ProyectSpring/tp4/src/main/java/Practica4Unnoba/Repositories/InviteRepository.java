
package Practica4Unnoba.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Practica4Unnoba.Entities.Invite;


public interface InviteRepository extends JpaRepository<Invite, Long> {

	@Query("select count(i) from Invite i where i.usuario.id = :userId and i.evento.id = :eventId")
	public int isInvited(@Param("eventId") long eventId, @Param("userId") long userId);
}
