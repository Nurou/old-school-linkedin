package projekti.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  private List<Account> results;

  public void AccountController() {
    this.results = new ArrayList<>();
  }

  @GetMapping("/home")
  public String homePage(final Model model) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();
    System.out.println("The user:  " + username + " logged in.");
    return "redirect:/profile/" + accountService.getAccountByUsername(username).getProfileName();
  }

  @GetMapping("/register")
  public String displayRegistrationPage(@ModelAttribute final Account account) {
    return "register";
  }

  @PostMapping("/register")
  public String register(@Valid @ModelAttribute final Account account, final BindingResult bindingResult) {
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
    } catch (final Exception e) {
      System.err.println(e.getMessage());
    }
    return "redirect:register";
  }

  @GetMapping("/profile/{profileName}")
  public String viewLoggedInProfile(final Model model, @PathVariable final String profileName) {
    final Account profile = accountService.getAccountByProfileName(profileName);
    model.addAttribute("profile", profile);
    Long imageId = imageService.getProfileImageId(profile);
    if (imageId != 0L) {
      model.addAttribute("imageId", imageId);
    }
    model.addAttribute("results", this.results);
    return "personal_profile";
  }

  @GetMapping("/users/profile/{profileName}")
  public String viewConnectedUsersPage(final Model model, @PathVariable final String profileName) {
    // cannot view profiles before connection
    // check if the target user has a connection

    // otherwise redirect back to current users page

    final Account profile = accountService.getAccountByProfileName(profileName);
    model.addAttribute("profile", profile);
    Long imageId = imageService.getProfileImageId(profile);
    if (imageId != 0L) {
      model.addAttribute("imageId", imageId);
    }
    model.addAttribute("results", this.results);
    return "profile";
  }

  @PostMapping("/users")
  public String add(@RequestParam final String searchTerm) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();
    this.results = accountService.getAccountsMatchingSearch(searchTerm);
    return "redirect:/profile/" + accountService.getAccountByUsername(username).getProfileName();
  }

}