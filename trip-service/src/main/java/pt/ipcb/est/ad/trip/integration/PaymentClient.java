package pt.ipcb.est.ad.trip.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "payment-cost-service")
public interface PaymentClient {

  @PostMapping("/costs/calculate")
  CostResponse calculate(@RequestBody CalculateCostRequest req);

  record CalculateCostRequest(
      UUID tripId,
      double distanceKm,
      String fuelType,
      double consumptionPer100,
      double tollCost,
      int occupants,
      String currency
  ) {}

  record CostResponse(UUID tripId, double fuelCost, double tollCost, double totalCost, double costPerPerson, String currency) {}
}
