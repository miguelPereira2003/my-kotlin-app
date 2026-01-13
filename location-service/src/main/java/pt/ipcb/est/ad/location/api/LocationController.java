package pt.ipcb.est.ad.location.api;

import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.location.api.dto.LocationDtos;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/location")
public class LocationController {

  @GetMapping("/geocode")
  public LocationDtos.GeocodeResponse geocode(@RequestParam String text) {
    // Simulação determinística: converte texto em coordenadas pseudo-aleatórias (Portugal approx.)
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] h = md.digest(text.getBytes(StandardCharsets.UTF_8));
      int a = ((h[0] & 0xff) << 8) | (h[1] & 0xff);
      int b = ((h[2] & 0xff) << 8) | (h[3] & 0xff);
      double lat = 37.0 + (a / 65535.0) * 5.0;  // 37..42
      double lng = -9.5 + (b / 65535.0) * 3.0;  // -9.5..-6.5
      return new LocationDtos.GeocodeResponse(lat, lng);
    } catch (Exception e) {
      return new LocationDtos.GeocodeResponse(39.8, -7.5);
    }
  }

  @GetMapping("/route")
  public LocationDtos.RouteResponse route(
      @RequestParam double originLat,
      @RequestParam double originLng,
      @RequestParam double destLat,
      @RequestParam double destLng
  ) {
    double distanceKm = haversine(originLat, originLng, destLat, destLng);
    int durationMin = (int) Math.max(1, Math.round((distanceKm / 60.0) * 60.0)); // 60 km/h avg
    return new LocationDtos.RouteResponse(distanceKm, durationMin);
  }

  private double haversine(double lat1, double lon1, double lat2, double lon2) {
    final double R = 6371.0;
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat/2)*Math.sin(dLat/2) +
        Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon/2)*Math.sin(dLon/2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c;
  }
}
