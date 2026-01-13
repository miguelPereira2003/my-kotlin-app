package pt.ipcb.est.ad.trip.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ipcb.est.ad.trip.domain.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
  List<Booking> findByTripId(UUID tripId);
  long countByTripIdAndStatus(UUID tripId, String status);
}
