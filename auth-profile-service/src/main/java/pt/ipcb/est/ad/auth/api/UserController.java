package pt.ipcb.est.ad.auth.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.auth.api.dto.AuthDtos;
import pt.ipcb.est.ad.auth.repo.UserRepository;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/me")
  public AuthDtos.MeResponse me(Authentication auth) {
    UUID userId = UUID.fromString(auth.getName());
    var user = userRepository.findById(userId).orElseThrow();
    return new AuthDtos.MeResponse(user.getId(), user.getEmail(), user.getName(), user.getRoles(), user.getStatus());
  }

  // Admin endpoints (used by admin-service)
  @GetMapping("/all")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public java.util.List<AuthDtos.UserSummary> allUsers() {
    return userRepository.findAll().stream()
        .map(u -> new AuthDtos.UserSummary(u.getId(), u.getEmail(), u.getName(), u.getRoles(), u.getStatus()))
        .toList();
  }

  @PostMapping("/{id}/block")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public void block(@PathVariable UUID id) {
    var u = userRepository.findById(id).orElseThrow();
    u.setStatus("BLOCKED");
    userRepository.save(u);
  }

  @PostMapping("/{id}/unblock")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public void unblock(@PathVariable UUID id) {
    var u = userRepository.findById(id).orElseThrow();
    u.setStatus("ACTIVE");
    userRepository.save(u);
  }
}
