package pt.ipcb.est.ad.location.api.dto;

public class LocationDtos {
  public record GeocodeResponse(double lat, double lng) {}
  public record RouteResponse(double distanceKm, int durationMin) {}
}
