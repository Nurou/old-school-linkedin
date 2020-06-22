package projekti.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import projekti.Account.Account;
import projekti.Account.AccountService;

@Controller
public class ConnectionController {

  @Autowired
  AccountService accountService;

  @Autowired
  ConnectionRepository connectionRepository;

  @PostMapping("/connections/new")
  public String add(@RequestParam final String requestSource, final String requestTarget) {

    Connection connection = new Connection();
    // TODO: refactor to use username or id
    Account source = accountService.getAccountByProfileName(requestSource);
    Account target = accountService.getAccountByProfileName(requestTarget);

    if (connectionRepository.findByRequestSourceAndRequestTarget(source, target) == null) {

      connection.setRequestSource(source);
      connection.setRequestTarget(target);
      connection.setAccepted(false);

      connectionRepository.save(connection);

    }

    System.out.println(connection);
    return "redirect:/profile/" + requestSource;
  }

  @PostMapping("/connections/accept")
  public String acceptConnection(@RequestParam Long requestSourceId, Long requestTargetId) {

    System.out.println("CONNECTION ACCEPTED");

    Account source = accountService.getById(requestSourceId);
    Account target = accountService.getById(requestTargetId);

    System.out.println(source.getId());
    System.out.println(target.getId());

    // get existing connection
    Connection connection = connectionRepository.findByRequestSourceAndRequestTarget(source, target);

    // System.out.println(connection);

    // // change status of accepted to true
    if (connection != null) {
      connection.setAccepted(true);
      connectionRepository.save(connection);
    }

    return "redirect:/profile/" + target.getProfileName();
  }

}