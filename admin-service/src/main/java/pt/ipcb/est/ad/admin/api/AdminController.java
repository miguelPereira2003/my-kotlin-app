package pt.ipcb.est.ad.admin.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.admin.integration.AuthClient;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final AuthClient authClient;

  public AdminController(AuthClient authClient) {
    this.authClient = authClient;
  }

  @GetMapping("/users")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public List<AuthClient.UserSummary> users() {
    return authClient.allUsers();
  }

  @PostMapping("/users/{id}/block")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public void block(@PathVariable UUID id) {
    authClient.block(id);
  }
}
