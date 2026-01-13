package pt.ipcb.est.ad.vehicle.security;

import java.util.UUID;

public record JwtPrincipal(UUID userId, String roles) {}
