package pt.ipcb.est.ad.auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.ipcb.est.ad.auth.domain.User;
import pt.ipcb.est.ad.auth.repo.UserRepository;

@Configuration
public class SeedConfig {

  @Bean
  CommandLineRunner seedAdmin(UserRepository repo, PasswordEncoder encoder) {
    return args -> {
      String adminEmail = "admin@local";
      if (!repo.existsByEmail(adminEmail)) {
        var user = User.create(adminEmail, encoder.encode("Admin123!"), "Admin", "ROLE_ADMIN");
        repo.save(user);
      }
    };
  }
}
