package Practica4Unnoba.Services;

import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Repositories.EventRepository;

@Service
public class EventService {
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private UserService userService;
	
	public List<Event> findAllEvents() {
		return eventRepository.findAll();
	}
	
	public List<Event> findAllEventsOrderByDate(){
		return eventRepository.findAllEventsOrderByEventDate();
	}
	
	public List<Event> findAllEventByOwnerIdOrderByDate(long UserID){
		return eventRepository.findAllEventByOwnerOrderByDate(userService.getUserById(UserID));
	}
	
	public List<Event> findAllEventWithInvitationsByUserId(long userId){
		return eventRepository.findAllEventsWithInvitationsByUserId(userId);
	}
	
	public boolean haveRegistration (Long eventId) {
		 return registrationService.eventHaveRegistration(eventId);
	}
	
	//espacios disponibles de un determinado evento
	public int spaceAvailable(Event event) {
		return event.getCapacity() - registrationService.quantityOfRegistrationByEvent(event.getId());
	}
	
	//calculo los espacios disponibles de cada evento y los guardo en un array
	public List<Integer> getSpacesAvailables(List<Event>events){
		List<Integer> spacesAvailables = new ArrayList<Integer>();
		
		for (Event e : events) {
			int numberOfRegistrations = registrationService.quantityOfRegistrationByEvent(e.getId());
			int spaceAvailable = (e.getCapacity() - numberOfRegistrations);
			spacesAvailables.add(spaceAvailable);
		}
		return spacesAvailables;
	}
	
	public boolean isFree (Event event) {
		//consigo el wrapper Float para usar el equals
		Float cost = event.getCost();
		return (cost.equals(0.0f)); 
		
	}
	
	//retorna su correspondiente mensaje de error en caso de haberlo, sino null
	public String validateEdit(Event event) {
		//wrapper costo del evento nuevo a guardar, proveniente de la vista
		Float cost = event.getCost();
		//wrapper capacidad del evento nuevo a guardar, proveniente de la vista
		Integer capacity = event.getCapacity();
		//evento en BD, datos todavía sin actualizar
		Event eventBD = this.getEvent(event.getId());
		
		//si quiero modificar el costo
		if(!cost.equals(eventBD.getCost())) {
			if(this.validateCost(event)!=null) {
				return this.validateCost(event);
			}
		}
		
		//si quiero modificar la capacidad
		if(!capacity.equals(eventBD.getCapacity())) {
			if(this.validateCapacity(event)!= null) {
				return this.validateCapacity(event);
			}
		}
		
		return null;
	}
	
	//envio error si se quiere modificar un evento con registros
	public String validateCost(Event event) {
		String message = null;
		//si tiene registros no lo dejo editar el costo
		if(this.haveRegistration(event.getId())) {
			message = "<b>Error:</b> no se puede editar el costo debido a que ya hay usuarios registrados.";
		}
		return message;
	}
	
	//envio error si se quiere modificar un evento con capacidad inapropiada
	public String validateCapacity(Event event) {
		String message = null;
		Integer capacity = event.getCapacity();
		//si la capacidad que quieren ingresar es menor que la cantidad de registraciones no lo dejo
		if(capacity < registrationService.quantityOfRegistrationByEvent(event.getId())) {
			message = "<b>Error:</b> no se puede editar el costo debido a que ya hay usuarios registrados.";
		}
		return message;
	}
	
	
	
	public void addEvent (Event event) {
		eventRepository.save(event);
	}
	
	public Event getEvent (Long id) {
		return eventRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
	}
	
	public void deleteEvent(@PathVariable Long id) {
		eventRepository.deleteById(id);
	}
	
	public Event updateEvent(Event event ,Long id) {
		return eventRepository.findById(id)
				.map(ev -> {
					ev.setName(event.getName());
					ev.setEventDate(event.getEventDate());
					ev.setStartRegistrations(event.getStartRegistrations());
					ev.setEndRegistrations(event.getEndRegistrations());
					ev.setPlace(event.getPlace());
					ev.setCapacity(event.getCapacity());
					ev.setCost(event.getCost());
					ev.setPrivateEvent(event.isPrivateEvent());
					return eventRepository.save(ev);
				})
				.orElseGet(() -> {
					return eventRepository.save(event);
				});
		}		
}