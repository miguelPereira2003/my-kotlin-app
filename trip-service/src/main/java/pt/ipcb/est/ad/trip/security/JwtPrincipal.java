package pt.ipcb.est.ad.trip.security;

import java.util.UUID;

public record JwtPrincipal(UUID userId, String roles) {}
