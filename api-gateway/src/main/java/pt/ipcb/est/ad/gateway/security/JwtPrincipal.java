package pt.ipcb.est.ad.gateway.security;

import java.util.UUID;

public record JwtPrincipal(UUID userId, String roles) {}
