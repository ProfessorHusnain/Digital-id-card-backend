package eEarn.com.userAuth.Repository;

import eEarn.com.userAuth.Entity.RestPasswordRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PasswordRestRepository extends JpaRepository<RestPasswordRequest,Long> {


    Optional<RestPasswordRequest> findByRestKey(String restKey);
}
