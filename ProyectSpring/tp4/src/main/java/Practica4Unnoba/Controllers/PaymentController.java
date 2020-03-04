package Practica4Unnoba.Controllers;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import Practica4Unnoba.Entities.Event;
import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Registration;
import Practica4Unnoba.Entities.Usuario;
import Practica4Unnoba.Services.EventService;
import Practica4Unnoba.Services.PaymentService;
import Practica4Unnoba.Services.RegistrationService;
import Practica4Unnoba.Services.UserService;
@Controller
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	
	@GetMapping("/")
	public String showPaymentForm() {
		return "payment";
	}
	
	@PostMapping("/")
	public String addPayment(@Valid Payment payment,  BindingResult result,@ModelAttribute("eventId")String eventId) {
		
		//datos para crear la registracion
		Usuario user = userService.getUserLogged();
		Date date = new Date();
		Event event = eventService.getEvent(Long.parseLong(eventId));
		
		//creo y persisto la registracion
		Registration r = new Registration();
		r.setUser(user);
		r.setCreatedAt(date);
		r.setEvent(event);
		registrationService.addRegistration(r);
		
		//persisto el pago
		payment.setRegistration(r);
		paymentService.addPayment(payment);
		registrationService.addRegistration(r);

		return "correct-inscription";
	}
}