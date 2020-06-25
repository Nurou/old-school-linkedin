package projekti.Post;

import java.sql.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projekti.Account.Account;
import projekti.Comment.Comment;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AbstractPersistable<Long> {

  private String content;
  private Date datePosted;

  @OneToMany(mappedBy = "post")
  private List<Comment> comments;

  @ManyToMany(mappedBy = "likedPosts")
  private List<Account> likes;

}
