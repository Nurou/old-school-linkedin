package projekti.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired
  AccountRepository accountRepository;

  public Account getAccountByUsername(String username) {
    return accountRepository.findByUsername(username);
  }

  public Account getAccountByProfileName(String profileName) {
    return accountRepository.findByProfileName(profileName);
  }

  /* impose unique usernames */
  public boolean usernameIsValid(String userName) {
    return getAccountByUsername(userName) == null;
  }

  public void saveAccount(Account account) {
    accountRepository.save(account);
  }

}