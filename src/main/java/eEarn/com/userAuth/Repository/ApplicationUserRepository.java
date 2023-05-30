package eEarn.com.userAuth.Repository;

import eEarn.com.userAuth.Entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser,Long> {
    Optional<ApplicationUser> findApplicationUserByUsername(String username);
    Optional<ApplicationUser> findApplicationUserByPhoneNumber(String username);
    Optional<ApplicationUser> findApplicationUserByEmail(String username);


}
