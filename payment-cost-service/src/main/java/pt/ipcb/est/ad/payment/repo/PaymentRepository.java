package pt.ipcb.est.ad.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ipcb.est.ad.payment.domain.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
  List<Payment> findByPayerUserId(UUID payerUserId);
  List<Payment> findByTripId(UUID tripId);
}
