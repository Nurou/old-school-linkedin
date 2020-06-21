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

  @PostMapping("/connections/new")
  public String add(@RequestParam final String requestSource, final String requestTarget) {

    Connection connection = new Connection();

    connection.setRequestSource(accountService.getAccountByProfileName(requestSource));
    connection.setRequestTarget(accountService.getAccountByProfileName(requestTarget));
    connection.setAccepted(false);

    System.out.println(connection);
    return "redirect:/profile/" + requestSource;
  }

}