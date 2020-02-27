package Practica4Unnoba.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Usuario;


public interface EventRepository extends JpaRepository<Event, Long>{

	@Query("select e from Event e where e.owner = :owner order by e.eventDate")
	public List<Event> findAllEventByOwnerOrderByDate(@Param("owner") Usuario user);
	
	@Query("select e from Event e order by e.eventDate")
	public List<Event> findAllEventsOrderByEventDate();
}