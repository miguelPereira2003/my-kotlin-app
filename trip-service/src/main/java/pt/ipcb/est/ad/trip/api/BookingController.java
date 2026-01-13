package pt.ipcb.est.ad.trip.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.trip.api.dto.TripDtos;
import pt.ipcb.est.ad.trip.service.TripService;

import java.util.UUID;

@RestController
public class BookingController {

  private final TripService tripService;

  public BookingController(TripService tripService) {
    this.tripService = tripService;
  }

  @PostMapping("/trips/{tripId}/bookings")
  @PreAuthorize("hasAuthority('ROLE_PASSENGER') or hasAuthority('ROLE_ADMIN')")
  public TripDtos.BookingResponse book(@PathVariable UUID tripId, @RequestBody TripDtos.CreateBookingRequest req, Authentication auth) {
    UUID passengerId = UUID.fromString(auth.getName());
    var b = tripService.createBooking(tripId, passengerId, req.seatsRequested());
    return new TripDtos.BookingResponse(b.getId(), b.getTripId(), b.getPassengerUserId(), b.getSeatsRequested(), b.getStatus());
  }

  @PostMapping("/bookings/{bookingId}/approve")
  @PreAuthorize("hasAuthority('ROLE_DRIVER') or hasAuthority('ROLE_ADMIN')")
  public TripDtos.BookingResponse approve(@PathVariable UUID bookingId, Authentication auth) {
    UUID driverId = UUID.fromString(auth.getName());
    var b = tripService.approveBooking(bookingId, driverId);
    return new TripDtos.BookingResponse(b.getId(), b.getTripId(), b.getPassengerUserId(), b.getSeatsRequested(), b.getStatus());
  }
}
