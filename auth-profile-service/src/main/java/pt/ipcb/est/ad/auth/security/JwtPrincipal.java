package pt.ipcb.est.ad.auth.security;

import java.util.UUID;

public record JwtPrincipal(UUID userId, String roles) {}
