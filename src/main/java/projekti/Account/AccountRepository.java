package projekti.Account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Account findByUsername(String username);

  Account findByProfileName(String profileName);

  List<Account> findAllByProfileNameLike(String profileName);

}