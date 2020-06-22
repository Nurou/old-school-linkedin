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

import projekti.Connection.Connection;
import projekti.Connection.ConnectionRepository;
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

  @Autowired
  ConnectionRepository connectionRepository;

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

    final Account currentUser = accountService.getAccountByProfileName(profileName);

    model.addAttribute("profile", currentUser);
    Long imageId = imageService.getProfileImageId(currentUser);
    if (imageId != 0L) {
      model.addAttribute("imageId", imageId);
    }

    model.addAttribute("results", this.results);

    List<String> pending = new ArrayList<>();
    List<String> accepted = new ArrayList<>();
    for (Connection connection : connectionRepository.findAllByRequestSource(currentUser)) {
      if (connection.getAccepted() == false) {
        pending.add(connection.getRequestTarget().getProfileName());
      } else {
        accepted.add(connection.getRequestTarget().getProfileName());
      }
    }

    model.addAttribute("pending", pending);
    model.addAttribute("accepted", accepted);

    List<String> connectionRequests = new ArrayList<>();

    // add requests
    for (Connection connection : currentUser.getReceivedRequests()) {
      connectionRequests.add(connection.getRequestSource().getProfileName());
    }

    if (!connectionRequests.isEmpty()) {
      model.addAttribute("requests", connectionRequests);
    }

    List<Connection> connections = new ArrayList<>();
    connections = connectionRepository.findAllByRequestSource(currentUser);
    List<Account> connectedUsers = new ArrayList<>();
    for (Connection connection : connections) {
      connectedUsers.add(connection.getRequestTarget());
    }

    for (Connection connection : currentUser.getSentRequests()) {
      System.out.println(connection.getRequestTarget());
    }

    System.out.println("****************************");
    System.out.println(currentUser.getSentRequests());
    System.out.println("****************************");
    model.addAttribute("connections", connectedUsers);
    return "personal_profile";
  }

  @GetMapping("/users/{profileName}")
  public String viewFellowUser(final Model model, @PathVariable final String profileName) {

    // profile being viewed
    final Account profile = accountService.getAccountByProfileName(profileName);

    // current user's profile
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();
    final Account currentUser = accountService.getAccountByUsername(username);

    Connection existingConnection = connectionRepository.findByRequestSourceAndRequestTarget(currentUser, profile);

    // get connection status to said other user
    if (existingConnection == null) {
      model.addAttribute("connection", "uninitiated");
    } else if (existingConnection.getAccepted() == false) {
      model.addAttribute("connection", "pending");
    } else {
      model.addAttribute("connection", "accepted");
    }

    // add both logged in and viewed users to model
    model.addAttribute("profile", profile);
    model.addAttribute("current", currentUser);
    Long imageId = imageService.getProfileImageId(profile);
    if (imageId != 0L) {
      model.addAttribute("imageId", imageId);
    }
    // add search results
    model.addAttribute("results", this.results);
    return "foreign_profile";
  }

  @PostMapping("/users")
  public String add(@RequestParam final String searchTerm) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();

    // fetch all matches on search term and store them for use in model
    this.results = accountService.getAccountsMatchingSearch(searchTerm);

    // remove current user's profile from the results
    for (Account account : results) {
      if (account.getUsername().equals(username)) {
        results.remove(account);
      }
    }

    return "redirect:/profile/" + accountService.getAccountByUsername(username).getProfileName();
  }

}