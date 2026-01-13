package pt.ipcb.est.ad.vehicle.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
public class Vehicle {

  @Id
  private UUID id;

  @Column(name = "owner_user_id", nullable = false)
  private UUID ownerUserId;

  @Column(nullable = false)
  private String brand;

  @Column(nullable = false)
  private String model;

  @Column(nullable = false)
  private String plate;

  @Column(name = "fuel_type", nullable = false)
  private String fuelType; // GAS | DIESEL | ELECTRIC

  @Column(name = "consumption_per_100", nullable = false)
  private double consumptionPer100;

  @Column(nullable = false)
  private int seats;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public Vehicle() {}

  public static Vehicle create(UUID ownerUserId, String brand, String model, String plate, String fuelType, double consumptionPer100, int seats) {
    Vehicle v = new Vehicle();
    v.id = UUID.randomUUID();
    v.ownerUserId = ownerUserId;
    v.brand = brand;
    v.model = model;
    v.plate = plate;
    v.fuelType = fuelType;
    v.consumptionPer100 = consumptionPer100;
    v.seats = seats;
    v.createdAt = Instant.now();
    return v;
  }

  public UUID getId() { return id; }
  public UUID getOwnerUserId() { return ownerUserId; }
  public String getBrand() { return brand; }
  public String getModel() { return model; }
  public String getPlate() { return plate; }
  public String getFuelType() { return fuelType; }
  public double getConsumptionPer100() { return consumptionPer100; }
  public int getSeats() { return seats; }
  public Instant getCreatedAt() { return createdAt; }

  public void setBrand(String brand) { this.brand = brand; }
  public void setModel(String model) { this.model = model; }
  public void setPlate(String plate) { this.plate = plate; }
  public void setFuelType(String fuelType) { this.fuelType = fuelType; }
  public void setConsumptionPer100(double consumptionPer100) { this.consumptionPer100 = consumptionPer100; }
  public void setSeats(int seats) { this.seats = seats; }
}
