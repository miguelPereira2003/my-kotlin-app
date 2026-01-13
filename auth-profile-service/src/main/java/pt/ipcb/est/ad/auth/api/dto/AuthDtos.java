package pt.ipcb.est.ad.auth.api.dto;

import java.util.UUID;

public class AuthDtos {
  public record RegisterRequest(String email, String password, String name, String role) {}
  public record LoginRequest(String email, String password) {}
  public record AuthResponse(String token, UUID userId, String roles, String name, String email) {}
  public record MeResponse(UUID userId, String email, String name, String roles, String status) {}
  public record UserSummary(UUID userId, String email, String name, String roles, String status) {}
}
