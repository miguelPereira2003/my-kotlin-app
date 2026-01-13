package pt.ipcb.est.ad.admin.security;

import java.util.UUID;

public record JwtPrincipal(UUID userId, String roles) {}
