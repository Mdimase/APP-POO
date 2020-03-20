package Practica4Unnoba.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Registration;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.EventService;
import Practica4Unnoba.Services.RegistrationService;
import Practica4Unnoba.Services.UserService;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	@GetMapping("/{id}")
	public String showRegistrationForm(@PathVariable long id, Registration registration, Model model) {
		Event event = eventService.getEvent(id);
		int spaceAvailable = eventService.spaceAvailable(event);

		model.addAttribute("spaceAvailable", spaceAvailable);
		model.addAttribute("event", event);
		return "registration";
	}
	
	@GetMapping("/my")
	public String findMyAllRegistrations(Model model) {
		List<Registration> registrations = new ArrayList<Registration>();
		registrations.addAll(registrationService.findAllRegistrationsByUserId(userService.getUserLogged().getId()));
		model.addAttribute("registrations", registrations);
		return "my-registrations";
	}
	
	@PostMapping("/add/{eventId}")
	public String addRegistration(@Valid Registration registration, @PathVariable Long eventId, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		String view = "home";
		String message = null;
		String classMessage = null;
		
		//obtengo el evento al que se quieren inscribir
		Event event = eventService.getEvent(eventId);
		Usuario userLogged = userService.getUserLogged();
		Date date = new Date();
		int spaceAvailable = eventService.spaceAvailable(event);
		
		registration.setUser(userLogged);
		registration.setEvent(event);
		registration.setCreatedAt(date);
		
		//validacion de fecha, owner, cupos, alreadyregister, haveInvitation
		message = registrationService.validate(registration, event);
		if(message != null) {
			classMessage = "alert-danger";
			redirectAttributes.addFlashAttribute("classMessage", classMessage);	
			redirectAttributes.addFlashAttribute("message", message);
			model.addAttribute("event", event);
			model.addAttribute("spaceAvailable", spaceAvailable);
			view="redirect:/registrations/" + eventId;
			return view;
		}
		
		//evento gratis
		if(eventService.isFree(event)) {
			registrationService.addRegistration(registration);
			
			message = "Te has registrado al evento correctamente.";
			classMessage = "alert-success";
			redirectAttributes.addFlashAttribute("classMessage", classMessage);
			redirectAttributes.addFlashAttribute("message", message);
			view = "redirect:/registrations/my";
			return view;
		}
			
		//evento pago
		else {
			Payment payment = new Payment();
			model.addAttribute("payment", payment);
			model.addAttribute("eventId", eventId);
			view = "payment";
			return view;
		}
	}
}