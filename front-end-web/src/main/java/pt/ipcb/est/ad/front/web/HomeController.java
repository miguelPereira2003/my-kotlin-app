package pt.ipcb.est.ad.front.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ipcb.est.ad.front.service.ApiClient;

import java.util.Map;

@Controller
public class HomeController {

  private final ApiClient api;

  public HomeController(ApiClient api) {
    this.api = api;
  }

  @GetMapping("/")
  public String home(HttpSession session, Model model) {
    String jwt = (String) session.getAttribute("jwt");
    model.addAttribute("loggedIn", jwt != null);
    return "index";
  }

  @GetMapping("/trips/search")
  public String searchPage() {
    return "search";
  }

  @PostMapping("/trips/search")
  public String doSearch(@RequestParam String origin, @RequestParam String destination, HttpSession session, Model model) {
    String jwt = (String) session.getAttribute("jwt");
    var resp = api.get("/trips/search?origin=" + origin + "&destination=" + destination, jwt, Map.class);
    model.addAttribute("trips", resp.get("trips"));
    return "search";
  }
}
