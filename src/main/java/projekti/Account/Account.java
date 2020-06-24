package projekti.Account;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
import projekti.Post.Post;
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

  // the list of skills this user has listed as theirs
  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Skill> skills;

  // the list of skills that the user has endorsed
  @JoinTable(name = "Skill_Endorsement", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
  @ManyToMany
  List<Skill> endorsements;

  @JoinTable(name = "Post_Like", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"))
  @ManyToMany
  List<Post> likedPosts;
}
