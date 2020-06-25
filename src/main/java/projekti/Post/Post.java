package projekti.Post;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import projekti.Account.Account;
import projekti.Comment.Comment;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post extends AbstractPersistable<Long> {

  private String content;

  @ToString.Exclude
  @Temporal(TemporalType.DATE)
  @Column(name = "LastTouched", insertable = false, updatable = false)
  private java.util.Date publishedOn;

  @OneToMany(mappedBy = "post")
  private List<Comment> comments;

  @ManyToMany(mappedBy = "likedPosts")
  private List<Account> likes;

  @ToString.Exclude
  @ManyToOne
  Account account;

}
