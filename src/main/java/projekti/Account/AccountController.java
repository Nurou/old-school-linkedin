package projekti.Account;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projekti.Image.ImageService;

@Controller
public class AccountController {

  /* TODO: abstract all interaction with repository to service */
  @Autowired
  AccountRepository accountRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AccountService accountService;

  @Autowired
  ImageService imageService;

  @GetMapping("/home")
  public String homePage(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    System.out.println("The user:  " + username + " logged in.");
    return "redirect:/users/profile/" + accountService.getAccountByUsername(username).getProfileName();
  }

  @GetMapping("/register")
  public String displayRegistrationPage(@ModelAttribute Account account) {
    return "register";
  }

  @PostMapping("/register")
  public String register(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "register";
    }
    System.out.println(bindingResult);
    try {
      // validate username
      if (accountService.usernameIsValid(account.getUsername())) {
        // encode password
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        // add account
        accountService.saveAccount(account);
        // redirect
        return "redirect:/login";
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return "redirect:register";
  }

  @GetMapping("/users/profile/{profileName}")
  public String viewProfilePage(Model model, @PathVariable String profileName) {
    Account profile = accountService.getAccountByProfileName(profileName);
    model.addAttribute("profile", profile);
    model.addAttribute("imageId", imageService.getProfileImageId(profile));
    System.out.println("*********************");
    System.out.println(imageService.getProfileImageId(profile));
    System.out.println("*********************");
    // System.out.println(accountService.getAccountByProfileName(profileName));
    return "profile";
  }

  @GetMapping("/users")
  public String list(Model model) {
    model.addAttribute("users", accountRepository.findAll());
    System.out.println(accountRepository.findAll());
    return "users";
  }

  @PostMapping("/users")
  public String add(@RequestParam String username, @RequestParam String password) {
    if (accountRepository.findByUsername(username) != null) {
      return "redirect:/users";
    }

    return "redirect:/users";
  }

}