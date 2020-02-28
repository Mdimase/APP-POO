package Practica4Unnoba.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Practica4Unnoba.Entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	@Query("select p from Payment p where p.registration.id = :registrationId")
	public Payment getPaymentByRegistration(@Param("registrationId") long registrationId);

}