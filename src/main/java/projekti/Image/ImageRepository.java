package projekti.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projekti.Account.Account;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
  Image findByAccount(Account Account);
}