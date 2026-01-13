package pt.ipcb.est.ad.payment.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.payment.api.dto.PaymentDtos;
import pt.ipcb.est.ad.payment.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/{tripId}/pay")
  public PaymentDtos.PaymentResponse pay(@PathVariable UUID tripId, Authentication auth) {
    UUID userId = UUID.fromString(auth.getName());
    var p = paymentService.pay(tripId, userId);
    return new PaymentDtos.PaymentResponse(p.getId(), p.getTripId(), p.getPayerUserId(), p.getAmount(), p.getStatus());
  }

  @GetMapping("/history")
  public PaymentDtos.HistoryResponse history(Authentication auth) {
    UUID userId = UUID.fromString(auth.getName());
    var items = paymentService.history(userId).stream()
        .map(p -> new PaymentDtos.PaymentResponse(p.getId(), p.getTripId(), p.getPayerUserId(), p.getAmount(), p.getStatus()))
        .toList();
    return new PaymentDtos.HistoryResponse(items);
  }
}
