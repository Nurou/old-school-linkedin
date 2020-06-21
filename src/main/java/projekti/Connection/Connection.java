package projekti.Connection;

import javax.persistence.Entity;
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
public class Connection extends AbstractPersistable<Long> {
  private Boolean accepted;

  @ManyToOne
  private Account requestSource;
  @ManyToOne
  private Account requestTarget;
}