package pt.ipcb.est.ad.trip.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class JwtService {
  private final byte[] secret;

  public JwtService(@Value("${app.jwt.secret}") String secret) {
    this.secret = secret.getBytes(StandardCharsets.UTF_8);
  }

  public JwtPrincipal parse(String token) {
    var claims = Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(secret))
        .build()
        .parseSignedClaims(token)
        .getPayload();

    UUID userId = UUID.fromString(claims.getSubject());
    String roles = claims.get("roles", String.class);
    return new JwtPrincipal(userId, roles == null ? "" : roles);
  }
}
