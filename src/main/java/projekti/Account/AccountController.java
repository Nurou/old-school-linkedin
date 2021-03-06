package projekti.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.xpath.SourceTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import projekti.Skill.Skill;
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

  @GetMapping("/home")
  public String homePage(final Model model) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();
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
      if (accountService.usernameIsValid(account.getUsername())) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountService.saveAccount(account);
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

    // add logged in user
    model.addAttribute("profile", currentUser);

    Long imageId = imageService.getProfileImageId(currentUser);
    // pass down image to template if one exists
    if (imageId != 0L) {
      model.addAttribute("imageId", imageId);
    }

    // if search term, limit results
    if (searchTerm != null && !searchTerm.isEmpty()) {
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
    model.addAttribute("skills", skillsRepository.findAllByAccount(currentUser));

    return "personal_profile";
  }

  @GetMapping("/users/{profileName}")
  public String viewFellowUser(final Model model, @PathVariable final String profileName,
      @PathVariable(required = false) Long endorsementSourceId,
      @PathVariable(required = false) Long endorsementTargetId) {

    // current user's profile
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    final String username = auth.getName();
    final Account currentUser = accountService.getAccountByUsername(username);
    // get connection names
    List<Account> currentUserConnections = connectionService.getConnectedAccountsByUserId(currentUser.getId());

    // see if user is in the list
    Boolean isConnected = false;
    for (Account connection : currentUserConnections) {
      if (connection.getProfileName().equals(profileName)) {
        isConnected = true;
      }
    }

    model.addAttribute("connected", isConnected);

    // profile being viewed
    final Account profile = accountService.getAccountByProfileName(profileName);

    // add both logged in and viewed users to model
    model.addAttribute("profile", profile);
    model.addAttribute("current", currentUser);
    Long imageId = imageService.getProfileImageId(profile);
    if (imageId != 0L) {
      model.addAttribute("imageId", imageId);
    }

    List<Skill> topSkills = skillsRepository.findAllByAccount(profile).stream().limit(3L).sorted((o1, o2) -> {
      return o1.getEndorsers().size() - o2.getEndorsers().size();
    }).collect(Collectors.toList());

    model.addAttribute("skills", topSkills);

    if (!topSkills.isEmpty()) {
      model.addAttribute("otherSkills", skillsRepository.findAllByAccount(profile));
    }

    return "foreign_profile";
  }

}