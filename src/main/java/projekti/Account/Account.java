package projekti.Account;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
}
