package pt.ipcb.est.ad.trip.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ipcb.est.ad.trip.domain.Booking;
import pt.ipcb.est.ad.trip.domain.Trip;
import pt.ipcb.est.ad.trip.integration.LocationClient;
import pt.ipcb.est.ad.trip.integration.PaymentClient;
import pt.ipcb.est.ad.trip.integration.VehicleClient;
import pt.ipcb.est.ad.trip.repo.BookingRepository;
import pt.ipcb.est.ad.trip.repo.TripRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TripService {

  private final TripRepository tripRepository;
  private final BookingRepository bookingRepository;
  private final LocationClient locationClient;
  private final PaymentClient paymentClient;
  private final VehicleClient vehicleClient;

  public TripService(TripRepository tripRepository, BookingRepository bookingRepository,
                     LocationClient locationClient, PaymentClient paymentClient, VehicleClient vehicleClient) {
    this.tripRepository = tripRepository;
    this.bookingRepository = bookingRepository;
    this.locationClient = locationClient;
    this.paymentClient = paymentClient;
    this.vehicleClient = vehicleClient;
  }

  @Transactional
  public Trip createTrip(Trip trip) {
    // If coordinates are present, compute route (distance/duration) with circuit breaker
    if (trip.getOriginLat() != null && trip.getOriginLng() != null && trip.getDestLat() != null && trip.getDestLng() != null) {
      var route = routeWithCb(trip.getOriginLat(), trip.getOriginLng(), trip.getDestLat(), trip.getDestLng());
      if (route != null) {
        trip.setEstimatedDistanceKm(route.distanceKm());
        trip.setEstimatedDurationMin(route.durationMin());
      }
    }
    return tripRepository.save(trip);
  }

  public List<Trip> search(String origin, String destination) {
    return tripRepository.findByOriginTextContainingIgnoreCaseAndDestinationTextContainingIgnoreCase(origin, destination);
  }

  @Transactional
  public Booking createBooking(UUID tripId, UUID passengerUserId, int seatsRequested) {
    Trip trip = tripRepository.findById(tripId).orElseThrow();
    if (!"OPEN".equals(trip.getStatus())) {
      throw new IllegalStateException("Viagem não está aberta a reservas.");
    }
    if (seatsRequested < 1 || seatsRequested > trip.getAvailableSeats()) {
      throw new IllegalArgumentException("Número de lugares inválido.");
    }
    Booking b = Booking.create(tripId, passengerUserId, seatsRequested);
    return bookingRepository.save(b);
  }

  @Transactional
  public Booking approveBooking(UUID bookingId, UUID driverUserId) {
    Booking b = bookingRepository.findById(bookingId).orElseThrow();
    Trip trip = tripRepository.findById(b.getTripId()).orElseThrow();
    if (!trip.getDriverUserId().equals(driverUserId)) {
      throw new SecurityException("Apenas o condutor pode aprovar.");
    }
    if (!"PENDING".equals(b.getStatus())) {
      throw new IllegalStateException("Reserva não está pendente.");
    }
    if (b.getSeatsRequested() > trip.getAvailableSeats()) {
      throw new IllegalStateException("Não há lugares suficientes.");
    }
    b.setStatus("APPROVED");
    tripRepository.save(trip); // keep for future
    // reduce seats
    trip = tripRepository.findById(trip.getId()).orElseThrow();
    trip.setStatus(trip.getStatus());
    tripRepository.save(trip);

    // NOTE: for simplicity we do not decrement seats in db column; you can extend to do so.
    return bookingRepository.save(b);
  }

  @Transactional
  public Trip startTrip(UUID tripId, UUID driverUserId) {
    Trip trip = tripRepository.findById(tripId).orElseThrow();
    if (!trip.getDriverUserId().equals(driverUserId)) throw new SecurityException("Apenas o condutor pode iniciar.");
    trip.setStatus("IN_PROGRESS");
    return tripRepository.save(trip);
  }

  @Transactional
  public PaymentClient.CostResponse completeTrip(UUID tripId, UUID driverUserId) {
    Trip trip = tripRepository.findById(tripId).orElseThrow();
    if (!trip.getDriverUserId().equals(driverUserId)) throw new SecurityException("Apenas o condutor pode concluir.");
    trip.setStatus("COMPLETED");
    tripRepository.save(trip);

    // Occupants: driver + approved bookings count
    long approved = bookingRepository.countByTripIdAndStatus(tripId, "APPROVED");
    int occupants = (int) (1 + approved);

    // Vehicle details (optional)
    String fuelType = "GAS";
    double consumption = 7.0;
    if (trip.getVehicleId() != null) {
      var v = vehicleClient.get(trip.getVehicleId());
      fuelType = v.fuelType();
      consumption = v.consumptionPer100();
    }

    double distance = trip.getEstimatedDistanceKm() != null ? trip.getEstimatedDistanceKm() : 50.0;

    return calculateCostWithCb(new PaymentClient.CalculateCostRequest(
        tripId, distance, fuelType, consumption, trip.getTollCost(), occupants, "EUR"
    ));
  }

  @CircuitBreaker(name = "location", fallbackMethod = "routeFallback")
  public LocationClient.RouteResponse routeWithCb(double oLat, double oLng, double dLat, double dLng) {
    return locationClient.route(oLat, oLng, dLat, dLng);
  }

  public LocationClient.RouteResponse routeFallback(double oLat, double oLng, double dLat, double dLng, Throwable t) {
    return null; // keep distance null; trip can still exist
  }

  @CircuitBreaker(name = "payment", fallbackMethod = "paymentFallback")
  public PaymentClient.CostResponse calculateCostWithCb(PaymentClient.CalculateCostRequest req) {
    return paymentClient.calculate(req);
  }

  public PaymentClient.CostResponse paymentFallback(PaymentClient.CalculateCostRequest req, Throwable t) {
    // return a safe response; calculation pending
    return new PaymentClient.CostResponse(req.tripId(), 0, req.tollCost(), req.tollCost(), req.tollCost()/Math.max(1, req.occupants()), req.currency());
  }
}
