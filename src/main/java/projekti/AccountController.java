package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

  @Autowired
  AccountRepository accountRepository;

  // @Autowired
  // PasswordEncoder passwordEncoder;

  @GetMapping("/home")
  public String homepage(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    System.out.println("The user:  " + username + " logged in.");
    return "redirect:/profile/" + accountRepository.findByUsername(username).getProfileName();
  }

  @GetMapping("/accounts")
  public String list(Model model) {
    model.addAttribute("accounts", accountRepository.findAll());
    System.out.println(accountRepository.findAll());
    return "accounts";
  }

  @GetMapping("/register")
  public String register(Model model) {
    return "register";
  }

  @PostMapping("/accounts")
  public String add(@RequestParam String username, @RequestParam String password) {
    if (accountRepository.findByUsername(username) != null) {
      return "redirect:/accounts";
    }

    // Account a = new Account(username, passwordEncoder.encode(password),
    // "test-profile");
    // accountRepository.save(a);
    return "redirect:/accounts";
  }

}