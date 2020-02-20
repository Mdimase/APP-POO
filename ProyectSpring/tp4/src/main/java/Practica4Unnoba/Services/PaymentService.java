package Practica4Unnoba.Services;

import Practica4Unnoba.Repositories.PaymentRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Service;

import Practica4Unnoba.Entities.Payment;
import Practica4Unnoba.Entities.Usuario;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	
	public void addPayment(Payment payment) {
	    paymentRepository.save(payment);
	}

	public Payment getPayment(Long id) {
		return paymentRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
	}
	
	public List<Payment> findAllPayments(){
		return paymentRepository.findAll();
	}
}