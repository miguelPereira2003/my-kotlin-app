package pt.ipcb.est.ad.payment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ipcb.est.ad.payment.api.dto.PaymentDtos;
import pt.ipcb.est.ad.payment.domain.CostBreakdown;
import pt.ipcb.est.ad.payment.repo.CostBreakdownRepository;

import java.util.UUID;

@Service
public class CostService {

  private final CostBreakdownRepository repo;
  private final double fuelPricePerLiter;
  private final double energyPricePerKwh;

  public CostService(
      CostBreakdownRepository repo,
      @Value("${app.pricing.fuelPricePerLiter}") double fuelPricePerLiter,
      @Value("${app.pricing.energyPricePerKwh}") double energyPricePerKwh
  ) {
    this.repo = repo;
    this.fuelPricePerLiter = fuelPricePerLiter;
    this.energyPricePerKwh = energyPricePerKwh;
  }

  @Transactional
  public PaymentDtos.CostResponse calculate(PaymentDtos.CalculateCostRequest req) {
    double fuelCost = estimateFuelCost(req.distanceKm(), req.fuelType(), req.consumptionPer100());
    double total = fuelCost + Math.max(0, req.tollCost());
    int occupants = Math.max(1, req.occupants());
    double perPerson = total / occupants;

    CostBreakdown c = repo.findByTripId(req.tripId())
        .orElse(CostBreakdown.create(req.tripId(), fuelCost, req.tollCost(), total, perPerson, req.currency() == null ? "EUR" : req.currency()));

    // overwrite by saving a new entity only if not exists; otherwise keep the first calculation
    repo.save(c);

    return new PaymentDtos.CostResponse(req.tripId(), c.getFuelCost(), c.getTollCost(), c.getTotalCost(), c.getCostPerPerson(), c.getCurrency());
  }

  public PaymentDtos.CostResponse get(UUID tripId) {
    var c = repo.findByTripId(tripId).orElseThrow();
    return new PaymentDtos.CostResponse(c.getTripId(), c.getFuelCost(), c.getTollCost(), c.getTotalCost(), c.getCostPerPerson(), c.getCurrency());
  }

  private double estimateFuelCost(double distanceKm, String fuelType, double consumptionPer100) {
    double units = (Math.max(0, distanceKm) / 100.0) * Math.max(0, consumptionPer100);
    if ("ELECTRIC".equalsIgnoreCase(fuelType)) {
      return units * energyPricePerKwh;
    }
    return units * fuelPricePerLiter;
  }
}
