package projekti.Connection;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import projekti.Account.Account;
import projekti.Account.AccountController;
import projekti.Account.AccountService;

@Controller
public class ConnectionController {

  @Autowired
  AccountService accountService;

  @Autowired
  AccountController accountController;

  @Autowired
  ConnectionRepository connectionRepository;

  @Autowired
  ConnectionService connectionService;

  @PostMapping("/connections/new")
  public String add(@RequestParam final Long requestSourceId, final Long requestTargetId) {
    Connection connection = new Connection();

    Account source = accountService.getById(requestSourceId);
    Account target = accountService.getById(requestTargetId);

    if (connectionRepository.findByRequestSourceAndRequestTarget(source, target) == null
        && (source.getId() != target.getId())) {

      connection.setRequestSource(source);
      connection.setRequestTarget(target);
      connection.setAccepted(false);

      connectionRepository.save(connection);

    }

    // save connections
    source.setConnections(connectionService.getConnectedAccountsByUserId(source.getId()));
    target.setConnections(connectionService.getConnectedAccountsByUserId(target.getId()));
    accountService.saveAccount(source);
    accountService.saveAccount(target);

    return "redirect:/profile/" + source.getProfileName();
  }

  @PostMapping("/connections/accept")
  public String acceptConnection(@RequestParam Long requestSourceId, Long requestTargetId) {

    Account source = accountService.getById(requestSourceId);
    Account target = accountService.getById(requestTargetId);

    // get existing connection
    Connection connection = connectionRepository.findByRequestSourceAndRequestTarget(source, target);

    if (connection != null) {
      connection.setAccepted(true);
      connectionRepository.save(connection);
    }

    return "redirect:/profile/" + target.getProfileName();
  }

  @PostMapping("/connections/disconnect")
  public String disconnect(@RequestParam Long currentUserId, Long otherId) {

    Account source = accountService.getById(currentUserId);
    Account target = accountService.getById(otherId);

    // get connections for current user (who requested disconnection)
    List<Connection> connections = connectionService.getByUserId(currentUserId);

    // filter for ones that include the other user
    connections = connections.stream()
        .filter(conn -> conn.getRequestSource() == target || conn.getRequestSource() == source)
        .collect(Collectors.toList());

    // delete the filtered connection
    connectionService.removeById(connections.get(0).getId());

    return "redirect:/profile/" + target.getProfileName();
  }

}