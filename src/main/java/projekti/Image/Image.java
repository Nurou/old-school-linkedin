package projekti.Image;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import projekti.Account.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image extends AbstractPersistable<Long> {

  private String name;
  private String mediaType;
  private Long size;

  /* store to db as a 'large object' */
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] content;

  @OneToOne(mappedBy = "profileImage")
  private Account account;
}