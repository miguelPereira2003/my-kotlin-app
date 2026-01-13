package pt.ipcb.est.ad.auth.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

  @Id
  private UUID id;

  @Column(nullable = false, unique = true, length = 200)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(nullable = false, length = 200)
  private String name;

  @Column(nullable = false, length = 250)
  private String roles; // e.g. ROLE_DRIVER,ROLE_PASSENGER

  @Column(nullable = false, length = 20)
  private String status; // ACTIVE | BLOCKED

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public User() {}

  public static User create(String email, String passwordHash, String name, String roles) {
    User u = new User();
    u.id = UUID.randomUUID();
    u.email = email.toLowerCase().trim();
    u.passwordHash = passwordHash;
    u.name = name.trim();
    u.roles = roles;
    u.status = "ACTIVE";
    u.createdAt = Instant.now();
    return u;
  }

  public UUID getId() { return id; }
  public String getEmail() { return email; }
  public String getPasswordHash() { return passwordHash; }
  public String getName() { return name; }
  public String getRoles() { return roles; }
  public String getStatus() { return status; }
  public Instant getCreatedAt() { return createdAt; }

  public void setStatus(String status) { this.status = status; }
  public void setRoles(String roles) { this.roles = roles; }
}
