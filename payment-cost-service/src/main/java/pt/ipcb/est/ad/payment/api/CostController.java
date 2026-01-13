package pt.ipcb.est.ad.payment.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.payment.api.dto.PaymentDtos;
import pt.ipcb.est.ad.payment.service.CostService;

import java.util.UUID;

@RestController
@RequestMapping("/costs")
public class CostController {

  private final CostService costService;

  public CostController(CostService costService) {
    this.costService = costService;
  }

  // Internal-ish endpoint (still JWT protected)
  @PostMapping("/calculate")
  @PreAuthorize("hasAuthority('ROLE_DRIVER') or hasAuthority('ROLE_ADMIN')")
  public PaymentDtos.CostResponse calculate(@RequestBody PaymentDtos.CalculateCostRequest req) {
    return costService.calculate(req);
  }

  @GetMapping("/{tripId}")
  public PaymentDtos.CostResponse get(@PathVariable UUID tripId) {
    return costService.get(tripId);
  }
}
