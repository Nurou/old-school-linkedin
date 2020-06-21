package projekti.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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

}