package projekti;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import projekti.Account.Account;
import projekti.Account.AccountRepository;
import projekti.Account.AccountService;
import projekti.Connection.Connection;
import projekti.Connection.ConnectionService;
import projekti.Skill.Skill;
import projekti.Skill.SkillRepository;

/**
 * A class for loading simple fake data on start up
 */
@Component
public class DataLoader implements ApplicationRunner {

  @Autowired
  private AccountService accountService;

  @Autowired
  private ConnectionService connectionService;

  @Autowired
  private SkillRepository skillRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  public DataLoader(AccountService accountService) {
    this.accountService = accountService;
  }

  public void run(ApplicationArguments args) {
    // accounts
    Account account1 = new Account();
    account1.setUsername("nurou");
    account1.setPassword(passwordEncoder.encode("password"));
    account1.setProfileName("Joel Hassan");
    accountService.saveAccount(account1);

    Account account2 = new Account();
    account2.setUsername("tester1");
    account2.setPassword(passwordEncoder.encode("password"));
    account2.setProfileName("First Tester");
    accountService.saveAccount(account2);

    Account account3 = new Account();
    account3.setUsername("tester2");
    account3.setPassword(passwordEncoder.encode("password"));
    account3.setProfileName("Second Tester");
    accountService.saveAccount(account3);

    // Skill skill1 = new Skill();
    // skill1.setName("Web Dev");
    // skill1.setAccount(account1);
    // List<Account> endorsers1 = new ArrayList<>();
    // endorsers1.add(account2);
    // skill1.setEndorsers(endorsers1);
    // skillRepository.save(skill1);
  }
}