package pt.ipcb.est.ad.trip.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ipcb.est.ad.trip.domain.Trip;

import java.util.List;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {
  List<Trip> findByOriginTextContainingIgnoreCaseAndDestinationTextContainingIgnoreCase(String origin, String destination);
  List<Trip> findByDriverUserId(UUID driverUserId);
}
