package pt.ipcb.est.ad.payment.security;

import java.util.UUID;

public record JwtPrincipal(UUID userId, String roles) {}
