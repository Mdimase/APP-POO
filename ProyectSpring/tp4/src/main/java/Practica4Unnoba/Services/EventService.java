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
	
	//@Autowired
	//private RegistrationService registrationService;
	
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
	
	//calculo los espacios disponibles de cada evento y los guardo en un array
	public List<Integer> getSpacesAvailables(List<Event>events){
		List<Integer> spacesAvailables = new ArrayList<Integer>();
		
		for (Event e:events) {
			int numberOfRegistrations = registrationService.quantityOfRegistrationByEvent(e.getId());
			int spaceAvailable = (e.getCapacity() - numberOfRegistrations);
			spacesAvailables.add(spaceAvailable);
			}
		return spacesAvailables;
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