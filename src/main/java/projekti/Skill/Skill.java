package projekti.Skill;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projekti.Account.Account;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill extends AbstractPersistable<Long> {

  // name of skill
  private String name;
  // user the skill belongs to
  @ManyToOne
  private Account account;

  // all user's who've endorsed skill
  @ManyToMany(mappedBy = "endorsements")
  private List<Account> endorsers;

}