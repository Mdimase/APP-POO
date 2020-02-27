package Practica4Unnoba.Services;

import Practica4Unnoba.Repositories.PaymentRepository;
import Practica4Unnoba.Repositories.RegistrationRepository;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Registration;

@Service
public class RegistrationService {
	
	@Autowired
	private RegistrationRepository registrationRepository;
	
	@Autowired
	private PaymentService paymentService;
	
	public List<Registration> findAllRegistrationsByEventID(Long event_id) {
		List<Registration> registrations = registrationRepository.findAllByEventID(event_id);
		return registrations;
	}
	
	public void addRegistration(Registration registration) {
		registrationRepository.save(registration);
	}
	
	public Registration getRegistration(Long id) {
	    return registrationRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
	}
	
	public int quantityOfRegistrationByEvent(Long event_id) {
		return registrationRepository.quantityOfRegistrationByEvent(event_id);
	}
	
	public boolean isRegistered(Long event_id, Long user_id) {
		int registration = registrationRepository.isRegistered(event_id, user_id);
		if(registration>0) {
			return true;
		}
		return false;
	}
	
	public boolean isPayment(Registration registration) {
		for(Payment p : paymentService.findAllPayments()){
			Long reg = registration.getId();
			Long pReg = p.getRegistration().getId();
			if(reg.equals(pReg)) {
				return true;
			}
		}
		return false;
	}
}