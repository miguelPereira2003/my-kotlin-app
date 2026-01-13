package pt.ipcb.est.ad.vehicle.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ipcb.est.ad.vehicle.domain.Vehicle;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
  List<Vehicle> findByOwnerUserId(UUID ownerUserId);
}
