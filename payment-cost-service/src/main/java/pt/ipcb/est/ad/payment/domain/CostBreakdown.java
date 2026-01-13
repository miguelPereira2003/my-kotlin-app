package pt.ipcb.est.ad.payment.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cost_breakdowns")
public class CostBreakdown {

  @Id
  private UUID id;

  @Column(name = "trip_id", nullable = false, unique = true)
  private UUID tripId;

  @Column(name = "fuel_cost", nullable = false)
  private double fuelCost;

  @Column(name = "toll_cost", nullable = false)
  private double tollCost;

  @Column(name = "total_cost", nullable = false)
  private double totalCost;

  @Column(name = "cost_per_person", nullable = false)
  private double costPerPerson;

  @Column(nullable = false)
  private String currency;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public CostBreakdown() {}

  public static CostBreakdown create(UUID tripId, double fuelCost, double tollCost, double totalCost, double costPerPerson, String currency) {
    CostBreakdown c = new CostBreakdown();
    c.id = UUID.randomUUID();
    c.tripId = tripId;
    c.fuelCost = fuelCost;
    c.tollCost = tollCost;
    c.totalCost = totalCost;
    c.costPerPerson = costPerPerson;
    c.currency = currency;
    c.createdAt = Instant.now();
    return c;
  }

  public UUID getId() { return id; }
  public UUID getTripId() { return tripId; }
  public double getFuelCost() { return fuelCost; }
  public double getTollCost() { return tollCost; }
  public double getTotalCost() { return totalCost; }
  public double getCostPerPerson() { return costPerPerson; }
  public String getCurrency() { return currency; }
  public Instant getCreatedAt() { return createdAt; }
}
