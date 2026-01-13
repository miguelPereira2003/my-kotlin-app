package pt.ipcb.est.ad.vehicle.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.vehicle.api.dto.VehicleDtos;
import pt.ipcb.est.ad.vehicle.repo.VehicleRepository;

import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
public class InternalVehicleController {

  private final VehicleRepository repo;

  public InternalVehicleController(VehicleRepository repo) {
    this.repo = repo;
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_DRIVER') or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_PASSENGER')")
  public VehicleDtos.VehicleResponse get(@PathVariable UUID id) {
    var v = repo.findById(id).orElseThrow();
    return new VehicleDtos.VehicleResponse(v.getId(), v.getBrand(), v.getModel(), v.getPlate(), v.getFuelType(), v.getConsumptionPer100(), v.getSeats());
  }
}
