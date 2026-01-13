package pt.ipcb.est.ad.trip.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.trip.api.dto.TripDtos;
import pt.ipcb.est.ad.trip.domain.Trip;
import pt.ipcb.est.ad.trip.repo.TripRepository;
import pt.ipcb.est.ad.trip.service.TripService;

import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

  private final TripService tripService;
  private final TripRepository tripRepository;

  public TripController(TripService tripService, TripRepository tripRepository) {
    this.tripService = tripService;
    this.tripRepository = tripRepository;
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_DRIVER') or hasAuthority('ROLE_ADMIN')")
  public TripDtos.TripResponse create(@RequestBody TripDtos.CreateTripRequest req, Authentication auth) {
    UUID driverId = UUID.fromString(auth.getName());
    Trip trip = Trip.create(
        driverId,
        req.vehicleId(),
        req.originText(),
        req.destinationText(),
        req.originLat(),
        req.originLng(),
        req.destLat(),
        req.destLng(),
        req.departureTime(),
        req.availableSeats(),
        req.tollCost()
    );
    trip = tripService.createTrip(trip);
    return toResponse(trip);
  }

  @GetMapping("/search")
  public TripDtos.SearchResponse search(@RequestParam String origin, @RequestParam String destination) {
    var trips = tripService.search(origin, destination).stream().map(this::toResponse).toList();
    return new TripDtos.SearchResponse(trips);
  }

  @GetMapping("/{id}")
  public TripDtos.TripResponse get(@PathVariable UUID id) {
    return toResponse(tripRepository.findById(id).orElseThrow());
  }

  @PostMapping("/{tripId}/start")
  @PreAuthorize("hasAuthority('ROLE_DRIVER') or hasAuthority('ROLE_ADMIN')")
  public TripDtos.TripResponse start(@PathVariable UUID tripId, Authentication auth) {
    UUID driverId = UUID.fromString(auth.getName());
    return toResponse(tripService.startTrip(tripId, driverId));
  }

  @PostMapping("/{tripId}/complete")
  @PreAuthorize("hasAuthority('ROLE_DRIVER') or hasAuthority('ROLE_ADMIN')")
  public Object complete(@PathVariable UUID tripId, Authentication auth) {
    UUID driverId = UUID.fromString(auth.getName());
    return tripService.completeTrip(tripId, driverId);
  }

  private TripDtos.TripResponse toResponse(Trip t) {
    return new TripDtos.TripResponse(
        t.getId(),
        t.getDriverUserId(),
        t.getVehicleId(),
        t.getOriginText(),
        t.getDestinationText(),
        t.getDepartureTime(),
        t.getAvailableSeats(),
        t.getStatus(),
        t.getEstimatedDistanceKm(),
        t.getEstimatedDurationMin(),
        t.getTollCost()
    );
  }
}
