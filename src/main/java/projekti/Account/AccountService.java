package projekti.Account;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired
  AccountRepository accountRepository;

  public Account getAccountByUsername(String username) {
    return accountRepository.findTopByUsername(username);
  }

  public Account getById(Long id) {
    return accountRepository.getOne(id);
  }

  public Account getAccountByProfileName(String profileName) {
    return accountRepository.findByProfileName(profileName);
  }

  public List<Account> getAccountsMatchingSearch(String searchTerm) {
    // return accountRepository.findAllByProfileNameLike("%" + searchTerm + "%");
    return accountRepository.findAll().stream().filter(acc -> {
      return acc.getProfileName().toLowerCase().contains(searchTerm.toLowerCase());
    }).collect(Collectors.toList());

  }

  public List<Account> getAll() {
    return accountRepository.findAll();
  }

  /* impose unique usernames */
  public boolean usernameIsValid(String userName) {
    return getAccountByUsername(userName) == null;
  }

  public void saveAccount(Account account) {
    if (usernameIsValid(account.getUsername())) {
      accountRepository.save(account);
    }
  }

  public void addAll(List<Account> accounts) {
    accountRepository.saveAll(accounts);
  }

}