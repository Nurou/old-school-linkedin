package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import projekti.Account.Account;
import projekti.Account.AccountRepository;
import projekti.Account.AccountService;

/**
 * A class for loading simple fake data on start up
 */
@Component
public class DataLoader implements ApplicationRunner {

  private AccountService accountService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  public DataLoader(AccountService accountService) {
    this.accountService = accountService;
  }

  public void run(ApplicationArguments args) {
    Account account1 = new Account();
    account1.setUsername("nurou");
    account1.setPassword(passwordEncoder.encode("password"));
    account1.setProfileName("Joel Hassan");
    accountService.saveAccount(account1);

    Account account2 = new Account();
    account2.setUsername("tester 1");
    account2.setPassword(passwordEncoder.encode("password"));
    account2.setProfileName("First Tester");
    accountService.saveAccount(account2);

    Account account3 = new Account();
    account3.setUsername("tester 2");
    account3.setPassword(passwordEncoder.encode("password"));
    account3.setProfileName("Second Tester");
    accountService.saveAccount(account3);
  }
}