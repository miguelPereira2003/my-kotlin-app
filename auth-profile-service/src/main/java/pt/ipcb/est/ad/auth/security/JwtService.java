package pt.ipcb.est.ad.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

  private final byte[] secret;
  private final long expMinutes;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expirationMinutes}") long expMinutes
  ) {
    this.secret = secret.getBytes(StandardCharsets.UTF_8);
    this.expMinutes = expMinutes;
  }

  public String generate(UUID userId, String roles) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(expMinutes * 60);
    return Jwts.builder()
        .subject(userId.toString())
        .claims(Map.of("roles", roles))
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .signWith(Keys.hmacShaKeyFor(secret), Jwts.SIG.HS256)
        .compact();
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
