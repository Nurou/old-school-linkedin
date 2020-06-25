package projekti.Skill;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projekti.Account.Account;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
  List<Skill> findAllByAccount(Account account, Pageable pageable);

  List<Skill> findAllByAccount(Account account);
}