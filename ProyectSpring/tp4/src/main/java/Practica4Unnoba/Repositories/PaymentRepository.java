package Practica4Unnoba.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import Practica4Unnoba.Entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


}