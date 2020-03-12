package Practica4Unnoba.Controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String addPayment(@Valid Payment payment,  BindingResult result, @ModelAttribute("eventId") String eventId, RedirectAttributes redirectAttributes) {
		
		//datos para crear la inscripción
		Usuario user = userService.getUserLogged();
		Date date = new Date();
		Event event = eventService.getEvent(Long.parseLong(eventId));
		
		//creo la inscripción y persisto la registration
		Registration r = new Registration();
		r.setUser(user);
		r.setCreatedAt(date);
		r.setEvent(event);
		registrationService.addRegistration(r);
		
		//persisto el pago
		payment.setRegistration(r);
		paymentService.addPayment(payment);
		
		String message = "Te has inscripto al evento correctamente.";
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/registrations/my";
	}
}