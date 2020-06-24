package projekti.Connection;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import projekti.Account.Account;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
  Connection findByRequestSourceAndRequestTarget(Account requestSource, Account requestTarget);

  List<Connection> findAllByRequestSource(Account requestSource);

  List<Connection> findByAcceptedTrue();
}
