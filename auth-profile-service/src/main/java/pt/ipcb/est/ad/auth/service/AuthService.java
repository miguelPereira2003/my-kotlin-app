package pt.ipcb.est.ad.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ipcb.est.ad.auth.api.dto.AuthDtos;
import pt.ipcb.est.ad.auth.domain.User;
import pt.ipcb.est.ad.auth.repo.UserRepository;
import pt.ipcb.est.ad.auth.security.JwtService;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @Transactional
  public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest req) {
    String email = req.email().toLowerCase().trim();
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email já existe.");
    }

    String roles = mapRole(req.role());
    String hash = passwordEncoder.encode(req.password());
    User user = User.create(email, hash, req.name(), roles);
    userRepository.save(user);

    String token = jwtService.generate(user.getId(), user.getRoles());
    return new AuthDtos.AuthResponse(token, user.getId(), user.getRoles(), user.getName(), user.getEmail());
  }

  public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req) {
    var user = userRepository.findByEmail(req.email().toLowerCase().trim())
        .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas."));

    if (!"ACTIVE".equals(user.getStatus())) {
      throw new IllegalArgumentException("Utilizador bloqueado.");
    }

    if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Credenciais inválidas.");
    }

    String token = jwtService.generate(user.getId(), user.getRoles());
    return new AuthDtos.AuthResponse(token, user.getId(), user.getRoles(), user.getName(), user.getEmail());
  }

  private String mapRole(String role) {
    if (role == null) return "ROLE_PASSENGER";

    return switch (role.trim().toUpperCase()) {
      case "DRIVER" -> "ROLE_DRIVER";
      case "PASSENGER" -> "ROLE_PASSENGER";
      case "BOTH" -> "ROLE_DRIVER,ROLE_PASSENGER";
      case "ADMIN" -> "ROLE_ADMIN";
      default -> "ROLE_PASSENGER";
    };
  }
}
