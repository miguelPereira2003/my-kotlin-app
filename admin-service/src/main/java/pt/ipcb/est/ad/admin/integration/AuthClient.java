package pt.ipcb.est.ad.admin.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "auth-profile-service")
public interface AuthClient {

  @GetMapping("/users/all")
  List<UserSummary> allUsers();

  @PostMapping("/users/{id}/block")
  void block(@PathVariable UUID id);

  record UserSummary(UUID userId, String email, String name, String roles, String status) {}
}
