package projekti.Connection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import projekti.Account.Account;
import projekti.Account.AccountRepository;

@Service
public class ConnectionService {

  @Autowired
  AccountRepository accountRepository;
  @Autowired
  ConnectionRepository connectionRepository;

  public List<Connection> getByUserId(Long id) {
    List<Connection> connections = new ArrayList<>();
    connections = connectionRepository.findAll();

    return connections.stream()
        .filter(conn -> (conn.getRequestSource().getId() == id || conn.getRequestTarget().getId() == id))
        .collect(Collectors.toList());

  }

  public void addAll(List<Connection> connections) {
    connectionRepository.saveAll(connections);
  }

  public void addOne(Connection connection) {
    connectionRepository.save(connection);
  }

  public List<Connection> getAcceptedConnectionsByUserId(Long id) {
    return getByUserId(id).stream().filter(conn -> conn.getAccepted() == true).collect(Collectors.toList());
  }

  public List<Connection> getUnacceptedConnectionsByUserId(Long id) {
    return getByUserId(id).stream().filter(conn -> conn.getAccepted() == false).collect(Collectors.toList());
  }

  public List<Account> getConnectedAccountsByUserId(Long id) {
    List<Connection> connections = getAcceptedConnectionsByUserId(id);
    Set<Account> accounts = new HashSet<>();
    for (Connection connection : connections) {
      if (connection.getRequestSource().getId() != id) {
        accounts.add(connection.getRequestSource());
      } else if (connection.getRequestTarget().getId() != id) {
        accounts.add(connection.getRequestTarget());
      }
    }
    List<Account> accountsList = new ArrayList<>();
    accountsList.addAll(accounts);
    accountsList = accountsList.stream().filter(acc -> acc.getId() != id).collect(Collectors.toList());
    return accountsList;
  }

  public List<Account> getPendingAccountsByUserId(Long id) {
    List<Connection> connections = getUnacceptedConnectionsByUserId(id);
    Set<Account> accounts = new HashSet<>();
    for (Connection connection : connections) {
      if (connection.getRequestSource().getId() != id) {
        accounts.add(connection.getRequestSource());
      } else if (connection.getRequestTarget().getId() != id) {
        accounts.add(connection.getRequestTarget());
      }
    }
    List<Account> accountsList = new ArrayList<>();
    accountsList.addAll(accounts);
    accountsList.stream().filter(acc -> acc.getId() != id).collect(Collectors.toList());
    return accountsList;
  }

  public void removeById(Long id) {
    connectionRepository.deleteById(id);
  }

}