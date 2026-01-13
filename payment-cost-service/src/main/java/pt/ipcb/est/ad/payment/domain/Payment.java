package pt.ipcb.est.ad.payment.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

  @Id
  private UUID id;

  @Column(name = "trip_id", nullable = false)
  private UUID tripId;

  @Column(name = "payer_user_id", nullable = false)
  private UUID payerUserId;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false, length = 20)
  private String status; // PENDING | PAID

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public Payment() {}

  public static Payment create(UUID tripId, UUID payerUserId, double amount, String status) {
    Payment p = new Payment();
    p.id = UUID.randomUUID();
    p.tripId = tripId;
    p.payerUserId = payerUserId;
    p.amount = amount;
    p.status = status;
    p.createdAt = Instant.now();
    return p;
  }

  public UUID getId() { return id; }
  public UUID getTripId() { return tripId; }
  public UUID getPayerUserId() { return payerUserId; }
  public double getAmount() { return amount; }
  public String getStatus() { return status; }
  public Instant getCreatedAt() { return createdAt; }

  public void setStatus(String status) { this.status = status; }
}
