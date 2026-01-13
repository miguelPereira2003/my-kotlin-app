package pt.ipcb.est.ad.trip.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trips")
public class Trip {

  @Id
  private UUID id;

  @Column(name = "driver_user_id", nullable = false)
  private UUID driverUserId;

  @Column(name = "vehicle_id")
  private UUID vehicleId;

  @Column(name = "origin_text", nullable = false)
  private String originText;

  @Column(name = "destination_text", nullable = false)
  private String destinationText;

  @Column(name = "origin_lat")
  private Double originLat;

  @Column(name = "origin_lng")
  private Double originLng;

  @Column(name = "dest_lat")
  private Double destLat;

  @Column(name = "dest_lng")
  private Double destLng;

  @Column(name = "departure_time", nullable = false)
  private LocalDateTime departureTime;

  @Column(name = "available_seats", nullable = false)
  private int availableSeats;

  @Column(nullable = false, length = 20)
  private String status; // OPEN | IN_PROGRESS | COMPLETED | CANCELLED

  @Column(name = "estimated_distance_km")
  private Double estimatedDistanceKm;

  @Column(name = "estimated_duration_min")
  private Integer estimatedDurationMin;

  @Column(name = "toll_cost", nullable = false)
  private double tollCost;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public Trip() {}

  public static Trip create(UUID driverUserId, UUID vehicleId, String originText, String destinationText,
                            Double originLat, Double originLng, Double destLat, Double destLng,
                            LocalDateTime departureTime, int availableSeats, double tollCost) {
    Trip t = new Trip();
    t.id = UUID.randomUUID();
    t.driverUserId = driverUserId;
    t.vehicleId = vehicleId;
    t.originText = originText;
    t.destinationText = destinationText;
    t.originLat = originLat;
    t.originLng = originLng;
    t.destLat = destLat;
    t.destLng = destLng;
    t.departureTime = departureTime;
    t.availableSeats = availableSeats;
    t.status = "OPEN";
    t.tollCost = Math.max(0, tollCost);
    t.createdAt = Instant.now();
    return t;
  }

  public UUID getId() { return id; }
  public UUID getDriverUserId() { return driverUserId; }
  public UUID getVehicleId() { return vehicleId; }
  public String getOriginText() { return originText; }
  public String getDestinationText() { return destinationText; }
  public Double getOriginLat() { return originLat; }
  public Double getOriginLng() { return originLng; }
  public Double getDestLat() { return destLat; }
  public Double getDestLng() { return destLng; }
  public LocalDateTime getDepartureTime() { return departureTime; }
  public int getAvailableSeats() { return availableSeats; }
  public String getStatus() { return status; }
  public Double getEstimatedDistanceKm() { return estimatedDistanceKm; }
  public Integer getEstimatedDurationMin() { return estimatedDurationMin; }
  public double getTollCost() { return tollCost; }

  public void setEstimatedDistanceKm(Double estimatedDistanceKm) { this.estimatedDistanceKm = estimatedDistanceKm; }
  public void setEstimatedDurationMin(Integer estimatedDurationMin) { this.estimatedDurationMin = estimatedDurationMin; }
  public void setStatus(String status) { this.status = status; }
}
