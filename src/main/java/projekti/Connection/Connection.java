package projekti.Connection;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import projekti.Account.Account;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Connection extends AbstractPersistable<Long> {

  private Boolean accepted;

  @ToString.Exclude
  @ManyToOne
  private Account requestSource;
  @ToString.Exclude
  @ManyToOne
  private Account requestTarget;
}