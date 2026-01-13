package pt.ipcb.est.ad.trip.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TripDtos {

  public record CreateTripRequest(
      UUID vehicleId,
      String originText,
      String destinationText,
      Double originLat,
      Double originLng,
      Double destLat,
      Double destLng,
      LocalDateTime departureTime,
      int availableSeats,
      double tollCost
  ) {}

  public record TripResponse(
      UUID id,
      UUID driverUserId,
      UUID vehicleId,
      String originText,
      String destinationText,
      LocalDateTime departureTime,
      int availableSeats,
      String status,
      Double estimatedDistanceKm,
      Integer estimatedDurationMin,
      double tollCost
  ) {}

  public record CreateBookingRequest(int seatsRequested) {}
  public record BookingResponse(UUID id, UUID tripId, UUID passengerUserId, int seatsRequested, String status) {}

  public record SearchResponse(List<TripResponse> trips) {}
}
