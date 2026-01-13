package pt.ipcb.est.ad.gateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter {

  private final JwtService jwtService;

  public JwtAuthFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();

    // ===== Public routes (NO JWT) =====
    // Auth endpoints must be public for register/login to work.
    // Actuator and Eureka should also be accessible without JWT.
    if (isPublicPath(path)) {
      return chain.filter(exchange);
    }

    // ===== Non-API routes (Front-end pages) =====
    // Front-end pages (Thymeleaf) are served via gateway under "/"
    // Keep them public.
    if (!path.startsWith("/api/")) {
      return chain.filter(exchange);
    }

    // ===== API routes (JWT required) =====
    String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (auth == null || !auth.startsWith("Bearer ")) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    try {
      JwtPrincipal principal = jwtService.parse(auth.substring(7));

      // Propagate user info to downstream services (optional convenience)
      ServerHttpRequest mutated = exchange.getRequest().mutate()
          .header("X-User-Id", principal.userId().toString())
          .header("X-Roles", principal.roles())
          .build();

      return chain.filter(exchange.mutate().request(mutated).build());
    } catch (Exception ex) {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }

  private boolean isPublicPath(String path) {
    // /api/auth or /api/auth/...
    if (path.equals("/api/auth") || path.startsWith("/api/auth/")) return true;

    // Actuator endpoints (health, info, metrics, etc.)
    if (path.equals("/actuator") || path.startsWith("/actuator/")) return true;

    // Eureka endpoints
    if (path.startsWith("/eureka")) return true;

    return false;
  }
}
