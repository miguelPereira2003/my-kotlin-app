package pt.ipcb.est.ad.trip.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "location-service")
public interface LocationClient {

  @GetMapping("/location/route")
  RouteResponse route(
      @RequestParam double originLat,
      @RequestParam double originLng,
      @RequestParam double destLat,
      @RequestParam double destLng
  );

  record RouteResponse(double distanceKm, int durationMin) {}
}
