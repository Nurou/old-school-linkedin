package projekti.Account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired
  AccountRepository accountRepository;

  public Account getAccountByUsername(String username) {
    return accountRepository.findByUsername(username);
  }

  public Account getById(Long id) {
    return accountRepository.getOne(id);
  }

  public Account getAccountByProfileName(String profileName) {
    return accountRepository.findByProfileName(profileName);
  }

  public List<Account> getAccountsMatchingSearch(String searchTerm) {
    return accountRepository.findAllByProfileNameLike("%" + searchTerm + "%");
  }

  /* impose unique usernames */
  public boolean usernameIsValid(String userName) {
    return getAccountByUsername(userName) == null;
  }

  public void saveAccount(Account account) {
    accountRepository.save(account);
  }

}