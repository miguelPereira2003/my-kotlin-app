package pt.ipcb.est.ad.trip.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {

  @GetMapping("/vehicles/{id}")
  VehicleResponse get(@PathVariable UUID id);

  record VehicleResponse(UUID id, String brand, String model, String plate, String fuelType, double consumptionPer100, int seats) {}
}
