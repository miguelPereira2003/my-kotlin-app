package pt.ipcb.est.ad.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ipcb.est.ad.payment.domain.Payment;
import pt.ipcb.est.ad.payment.repo.CostBreakdownRepository;
import pt.ipcb.est.ad.payment.repo.PaymentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final CostBreakdownRepository costRepository;

  public PaymentService(PaymentRepository paymentRepository, CostBreakdownRepository costRepository) {
    this.paymentRepository = paymentRepository;
    this.costRepository = costRepository;
  }

  @Transactional
  public Payment pay(UUID tripId, UUID payerUserId) {
    var cost = costRepository.findByTripId(tripId).orElseThrow();
    Payment p = Payment.create(tripId, payerUserId, cost.getCostPerPerson(), "PAID");
    return paymentRepository.save(p);
  }

  public List<Payment> history(UUID payerUserId) {
    return paymentRepository.findByPayerUserId(payerUserId);
  }
}
