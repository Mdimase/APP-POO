package Practica4Unnoba.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Practica4Unnoba.Entities.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
	
	@Query("select count(r) from Registration r where r.event.id = :event_id")
	public int quantityOfRegistrationByEvent(@Param("event_id") long event_id);
	
	@Query("select r from Registration r where r.event.id = :event_id")
	public List<Registration> findAllByEventID(@Param("event_id") long event_id);
	
	@Query("select count(r) from Registration r where r.user.id = :user_id and r.event.id = :event_id")
	public int isRegistered(@Param("event_id") long event_id, @Param("user_id") long user_id);
	
}