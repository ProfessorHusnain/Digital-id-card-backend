package eEarn.com.userAuth.Repository;/*
 *** Author: MUHAMMAD HUSNAIN
 */

import eEarn.com.userAuth.Entity.BusinessHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<BusinessHolder,Long> {

}
