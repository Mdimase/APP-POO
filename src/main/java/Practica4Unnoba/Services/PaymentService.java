package Practica4Unnoba.Services;

import Practica4Unnoba.Repositories.PaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Registration;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private EventService eventService;
	
	
	public void addPayment(Payment payment) {
	    paymentRepository.save(payment);
	}

	public Payment getPayment(Long id) {
		return paymentRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
	}
	
	public List<Payment> findAllPayments(){
		return paymentRepository.findAll();
	}
	
	public Payment getPaymentByRegistration(Long registrationId) {
		return paymentRepository.getPaymentByRegistration(registrationId);
	}
	
	//me devuelve una lista de todos los pagos que tienen las inscripciones
	public List<Payment> getPaymentsByRegistrations(List<Registration> registrations){
		List<Payment> payments = new ArrayList<Payment>();
		for(Registration registration : registrations) {
			if(!eventService.isFree(registration.getEvent())) {
				Payment payment = this.getPaymentByRegistration(registration.getId());
				payments.add(payment);
			}
		}
		return payments;
	}
}