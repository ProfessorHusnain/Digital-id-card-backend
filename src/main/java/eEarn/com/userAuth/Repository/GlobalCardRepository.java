package eEarn.com.userAuth.Repository;

import eEarn.com.userAuth.Entity.ApplicationUser;
import eEarn.com.userAuth.Entity.GlobalCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GlobalCardRepository extends JpaRepository<GlobalCard, Long> {
    Optional<GlobalCard> findByGlobalCardId(String id);
    List<GlobalCard> findByOwnerId(Long user);
    List<GlobalCard> findByVisitorId(Long user);
}
