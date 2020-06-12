package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Account findByUsername(String userName);

  Account getUserAccountById(Long id);
}