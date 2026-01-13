package pt.ipcb.est.ad.trip.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

  @Id
  private UUID id;

  @Column(name = "trip_id", nullable = false)
  private UUID tripId;

  @Column(name = "passenger_user_id", nullable = false)
  private UUID passengerUserId;

  @Column(name = "seats_requested", nullable = false)
  private int seatsRequested;

  @Column(nullable = false, length = 20)
  private String status; // PENDING | APPROVED | REJECTED | CANCELLED

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public Booking() {}

  public static Booking create(UUID tripId, UUID passengerUserId, int seatsRequested) {
    Booking b = new Booking();
    b.id = UUID.randomUUID();
    b.tripId = tripId;
    b.passengerUserId = passengerUserId;
    b.seatsRequested = seatsRequested;
    b.status = "PENDING";
    b.createdAt = Instant.now();
    return b;
  }

  public UUID getId() { return id; }
  public UUID getTripId() { return tripId; }
  public UUID getPassengerUserId() { return passengerUserId; }
  public int getSeatsRequested() { return seatsRequested; }
  public String getStatus() { return status; }

  public void setStatus(String status) { this.status = status; }
}
