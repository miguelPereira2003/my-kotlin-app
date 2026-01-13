package pt.ipcb.est.ad.payment.api.dto;

import java.util.List;
import java.util.UUID;

public class PaymentDtos {

  // Request from trip-service when concluding a trip
  public record CalculateCostRequest(
      UUID tripId,
      double distanceKm,
      String fuelType,
      double consumptionPer100,
      double tollCost,
      int occupants,
      String currency
  ) {}

  public record CostResponse(UUID tripId, double fuelCost, double tollCost, double totalCost, double costPerPerson, String currency) {}

  public record PayRequest(UUID tripId) {}
  public record PaymentResponse(UUID id, UUID tripId, UUID payerUserId, double amount, String status) {}

  public record HistoryResponse(List<PaymentResponse> payments) {}
}
