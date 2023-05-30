package eEarn.com.userAuth.Repository;

import eEarn.com.userAuth.Entity.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokensRepository extends JpaRepository<Tokens,Long> {
  Optional<Tokens> findByAuthenticationToken(String token);
  Optional<Tokens> findByUsername(String username);
}
