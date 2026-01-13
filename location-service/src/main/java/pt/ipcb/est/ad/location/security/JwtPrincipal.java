package pt.ipcb.est.ad.location.security;

import java.util.UUID;

public record JwtPrincipal(UUID userId, String roles) {}
