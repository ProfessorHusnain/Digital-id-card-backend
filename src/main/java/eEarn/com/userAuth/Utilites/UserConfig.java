package eEarn.com.userAuth.Utilites;

import eEarn.com.userAuth.Entity.ApplicationUser;
import eEarn.com.userAuth.Repository.ApplicationUserRepository;
import lombok.AllArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.transaction.annotation.Transactional;

import static eEarn.com.userAuth.Roles.ApplicationUserRole.ADMIN;

@Configuration
@AllArgsConstructor
public class UserConfig {

    private PasswordConfiguration passwordConfiguration;
    //private KafkaProducerForNotification kafkaProducerForNotification;

    @Bean
    @Transactional
    CommandLineRunner commandLineRunner(ApplicationUserRepository applicationUserRepository){
        return args -> {



      /* for (int i=0;i<1000000;i++){
           NotificationDto notificationDto= NotificationDto
                   .builder()
                   .id(i)
                   .build();
           kafkaProducerForNotification.send(notificationDto);
       }*/

            ApplicationUser applicationUser=ApplicationUser.builder()
                  .userRole(ADMIN)
                  .firstName("IUB")
                  .lastName("BHAWALNAGAR")
                    .username("iub")
                    .password(passwordConfiguration.passwordEncoder().encode("password"))
                  .isAccountNonExpired(true)
                  .isAccountNonLocked(true)
                  .isEnabled(true)
                  .isCredentialsNonExpired(true)
                  //.password(passwordConfiguration.passwordEncoder().encode("123"))
                     //.privacyPolicy(privacyPolicy)
                    .phoneNumber("123456")
                    .email("iub@pakistan.com")
                  .build();

            //***********************************************************************************************
            //applicationUserRepository.save(applicationUser);
            };

    }
}
