package pt.ipcb.est.ad.vehicle.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.vehicle.api.dto.VehicleDtos;
import pt.ipcb.est.ad.vehicle.domain.Vehicle;
import pt.ipcb.est.ad.vehicle.repo.VehicleRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

  private final VehicleRepository repo;

  public VehicleController(VehicleRepository repo) {
    this.repo = repo;
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_DRIVER') or hasAuthority('ROLE_ADMIN')")
  public VehicleDtos.VehicleResponse create(@RequestBody VehicleDtos.CreateVehicleRequest req, Authentication auth) {
    UUID userId = UUID.fromString(auth.getName());
    Vehicle v = Vehicle.create(userId, req.brand(), req.model(), req.plate(), req.fuelType(), req.consumptionPer100(), req.seats());
    v = repo.save(v);
    return new VehicleDtos.VehicleResponse(v.getId(), v.getBrand(), v.getModel(), v.getPlate(), v.getFuelType(), v.getConsumptionPer100(), v.getSeats());
  }

  @GetMapping("/mine")
  public List<VehicleDtos.VehicleResponse> mine(Authentication auth) {
    UUID userId = UUID.fromString(auth.getName());
    return repo.findByOwnerUserId(userId).stream()
        .map(v -> new VehicleDtos.VehicleResponse(v.getId(), v.getBrand(), v.getModel(), v.getPlate(), v.getFuelType(), v.getConsumptionPer100(), v.getSeats()))
        .toList();
  }
}
