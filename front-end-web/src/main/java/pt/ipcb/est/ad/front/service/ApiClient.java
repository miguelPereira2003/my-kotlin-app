package pt.ipcb.est.ad.front.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class ApiClient {

  private final RestClient client;
  private final String base;

  public ApiClient(@Value("${app.apiBase}") String base) {
    this.base = base;
    this.client = RestClient.create();
  }

  public <T> T post(String path, Object body, String jwt, Class<T> cls) {
    var req = client.post()
        .uri(base + path)
        .contentType(MediaType.APPLICATION_JSON)
        .body(body);
    if (jwt != null && !jwt.isBlank()) req = req.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
    return req.retrieve().body(cls);
  }

  public <T> T get(String path, String jwt, Class<T> cls) {
    var req = client.get().uri(base + path);
    if (jwt != null && !jwt.isBlank()) req = req.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
    return req.retrieve().body(cls);
  }

  public String getBase() { return base; }
}
