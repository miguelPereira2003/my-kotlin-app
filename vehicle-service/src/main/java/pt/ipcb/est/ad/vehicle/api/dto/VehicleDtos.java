package pt.ipcb.est.ad.vehicle.api.dto;

import java.util.UUID;

public class VehicleDtos {
  public record CreateVehicleRequest(String brand, String model, String plate, String fuelType, double consumptionPer100, int seats) {}
  public record VehicleResponse(UUID id, String brand, String model, String plate, String fuelType, double consumptionPer100, int seats) {}
}
