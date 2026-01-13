package pt.ipcb.est.ad.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ipcb.est.ad.payment.domain.CostBreakdown;

import java.util.Optional;
import java.util.UUID;

public interface CostBreakdownRepository extends JpaRepository<CostBreakdown, UUID> {
  Optional<CostBreakdown> findByTripId(UUID tripId);
}
