package projekti.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.xpath.SourceTree;
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
import projekti.Connection.ConnectionService;
import projekti.Image.ImageService;
import projekti.Skill.SkillRepository;

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
  SkillRepository skillsRepository;

  @Autowired
  ConnectionRepository connectionRepository;

  @Autowired
  ConnectionService connectionService;

  // private List<Account> results;

  // public void AccountController() {
  // this.results = new ArrayList<>();
  // }

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
  public String viewLoggedInProfile(final Model model, @PathVariable final String profileName,
      @RequestParam(required = false) String searchTerm) {

    final Account currentUser = accountService.getAccountByProfileName(profileName);

    model.addAttribute("profile", currentUser);
    Long imageId = imageService.getProfileImageId(currentUser);
    if (imageId != 0L) {
      model.addAttribute("imageId", imageId);
    }

    // if search term, limit results
    System.out.println(searchTerm);
    if (!searchTerm.isEmpty()) {
      model.addAttribute("results",
          accountService.getAll().stream()
              .filter(acc -> acc.getId() != currentUser.getId()
                  && acc.getProfileName().toLowerCase().contains(searchTerm.toLowerCase()))
              .collect(Collectors.toList()));
    } else {
      model.addAttribute("results", accountService.getAll().stream().filter(acc -> acc.getId() != currentUser.getId())
          .collect(Collectors.toList()));
    }

    // connection & not accepted
    List<Long> pending = new ArrayList<>();

    for (Connection connection : connectionRepository.findAllByRequestSource(currentUser)) {
      if (connection.getAccepted() == false) {
        pending.add(connection.getRequestTarget().getId());
      }
    }

    model.addAttribute("pending", pending);
    model.addAttribute("connections", connectionService.getConnectedAccountsByUserId(currentUser.getId()));
    // skills
    model.addAttribute("skills", skillsRepository.findByProfileId(currentUser.getId()));

    return "personal_profile";
  }

  @GetMapping("/users/{profileName}")
  public String viewFellowUser(final Model model, @PathVariable final String profileName) {

    // current user's profile
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();
    final Account currentUser = accountService.getAccountByUsername(username);
    // get connection names
    // see if user is in the list

    // profile being viewed
    final Account profile = accountService.getAccountByProfileName(profileName);
    System.out.println(profile);

    Connection existingConnection = connectionRepository.findByRequestSourceAndRequestTarget(currentUser, profile);

    // get connection status to said other user
    if (existingConnection == null) {
      model.addAttribute("connection", "uninitiated");
    } else if (existingConnection.getAccepted() == false) {
      model.addAttribute("connection", "pending");
    } else {
      model.addAttribute("connection", "accepted");
    }

    System.out.println(model);

    // add both logged in and viewed users to model
    model.addAttribute("profile", profile);
    model.addAttribute("current", currentUser);
    Long imageId = imageService.getProfileImageId(profile);
    if (imageId != 0L) {
      model.addAttribute("imageId", imageId);
    }

    // skills
    model.addAttribute("skills", skillsRepository.findByProfileId(profile.getId()));

    return "foreign_profile";
  }

  @PostMapping("/users/search")
  public String search(@RequestParam final String searchTerm) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();

    // // fetch all matches on search term and store them for use in model
    // this.results = accountService.getAccountsMatchingSearch(searchTerm);

    // List<Account> filteredResults = new ArrayList<>();

    // // remove current user's profile from the results
    // for (Account account : results) {
    // if (!account.getUsername().equals(username)) {
    // filteredResults.add(account);
    // }
    // }

    // this.results = filteredResults;

    return "redirect:/profile/" + accountService.getAccountByUsername(username).getProfileName();
  }

  @PostMapping("/users/all")
  public String getAll() {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();

    // fetch all matches on search term and store them for use in model
    // this.results = accountService.getAll().stream().filter(acc ->
    // !acc.getUsername().equals(username))
    // .collect(Collectors.toList());

    return "redirect:/profile/" + accountService.getAccountByUsername(username).getProfileName();
  }

}