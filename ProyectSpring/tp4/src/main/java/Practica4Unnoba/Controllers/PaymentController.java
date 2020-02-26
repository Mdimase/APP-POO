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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	
	
	@GetMapping("/payment/")
	public String showPaymentForm() {
		return "payment";
	}
	
	@PostMapping("/payment/")
	public String addPayment(@Valid Payment payment,  BindingResult result) {
		System.out.println("----------------" + payment.getCard());
		System.out.println("----------------" + Long.valueOf((String)result.getFieldValue("event")));
	
		//obtengo el usuario logueado
		Usuario user = userService.getUser();
		
		Registration r = new Registration();
		r.setUser(user);
		
		Date date = new Date();
		r.setCreatedAt(date);
		
		Event event = eventService.getEvent(Long.valueOf((String)result.getFieldValue("event")));
		r.setEvent(event);
		
		registrationService.addRegistration(r);
		
		payment.setRegistration(r);
		paymentService.addPayment(payment);
		
		registrationService.addRegistration(r);
		
		return "home";
	}
	
	@GetMapping ("/payment/{id}")
	public String getPayment(@PathVariable Long id, Model model) {
		Payment payment = paymentService.getPayment(id);
		
		model.addAttribute("payment", payment);
		
		return "payment";
	}
	
	@GetMapping("/payments")
	public String getPayments(Model model) {
		List<Payment> payments = paymentService.findAllPayments();
		
		model.addAttribute("payments", payments);
		
		return "payments";
	}
}