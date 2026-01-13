package pt.ipcb.est.ad.front.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.front.service.ApiClient;

import java.util.Map;

@Controller
public class AuthWebController {

  private final ApiClient api;

  public AuthWebController(ApiClient api) {
    this.api = api;
  }

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @PostMapping("/login")
  public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
    try {
      var resp = api.post("/auth/login", Map.of("email", email, "password", password), null, Map.class);
      session.setAttribute("jwt", (String) resp.get("token"));
      return "redirect:/";
    } catch (Exception ex) {
      model.addAttribute("error", "Login inválido.");
      return "login";
    }
  }

  @GetMapping("/register")
  public String registerPage() {
    return "register";
  }

  @PostMapping("/register")
  public String register(@RequestParam String name, @RequestParam String email, @RequestParam String password,
                         @RequestParam(defaultValue = "PASSENGER") String role,
                         HttpSession session, Model model) {
    try {
      var resp = api.post("/auth/register", Map.of("name", name, "email", email, "password", password, "role", role), null, Map.class);
      session.setAttribute("jwt", (String) resp.get("token"));
      return "redirect:/";
    } catch (Exception ex) {
      model.addAttribute("error", "Registo inválido (email pode já existir).");
      return "register";
    }
  }

  @PostMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
  }
}
