package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @GetMapping("/home")
  public String homepage(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    System.out.println("The user:  " + username + " logged in.");
    return "redirect:/users/profile/" + accountRepository.findByUsername(username).getProfileName();
  }

  @GetMapping("/users/profile/{profileName}")
  public String viewProfilePage(Model model, @PathVariable String profileName) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println(accountRepository.findByProfileName(profileName));
    return "profile";
  }

  @GetMapping("/users")
  public String list(Model model) {
    model.addAttribute("users", accountRepository.findAll());
    System.out.println(accountRepository.findAll());
    return "users";
  }

  @GetMapping("/register")
  public String register(Model model) {
    return "register";
  }

  @PostMapping("/users")
  public String add(@RequestParam String username, @RequestParam String password) {
    if (accountRepository.findByUsername(username) != null) {
      return "redirect:/users";
    }

    Account a = new Account(username, passwordEncoder.encode(password), "test-profile");
    accountRepository.save(a);
    return "redirect:/users";
  }

}