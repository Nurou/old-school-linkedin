package projekti.Account;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projekti.Comment.Comment;
import projekti.Image.Image;
import projekti.Skill.Skill;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account extends AbstractPersistable<Long> {

  @NotEmpty
  @Size(min = 3, max = 100)
  private String username;

  @NotEmpty
  @Size(min = 3, max = 100)
  private String password;

  @NotEmpty
  @Size(min = 3, max = 100)
  private String profileName;

  @OneToOne(cascade = CascadeType.ALL)
  private Image profileImage;

  // @OneToMany(mappedBy = "account")
  // private List<Comment> comments;

  // @ManyToMany(mappedBy = "account")
  // private List<Skill> skills;

  // @OneToMany
  // private List<Skill> endorsements;

}
