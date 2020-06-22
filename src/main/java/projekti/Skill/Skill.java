package projekti.Skill;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
  private Long profileId;

  // all user's who've endorsed skill
  // private List<Account> endorsers;

  // @ManyToMany
  // private List<Account> profiles;

}