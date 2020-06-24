package projekti.Account;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import projekti.Comment.Comment;
import projekti.Connection.Connection;
import projekti.Image.Image;
import projekti.Skill.Skill;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

  @ToString.Exclude
  @OneToOne(cascade = CascadeType.ALL)
  private Image profileImage;

  // all established connections
  @ManyToMany
  private List<Account> connections;

  @ToString.Exclude
  @OneToMany(mappedBy = "requestSource")
  private List<Connection> sentRequests;
  @ToString.Exclude
  @OneToMany(mappedBy = "requestTarget")
  private List<Connection> receivedRequests;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Skill> skills;

  // @OneToMany(mappedBy = "account")
  // private List<Comment> comments;

  // @OneToMany
  // private List<Skill> endorsements;

}
