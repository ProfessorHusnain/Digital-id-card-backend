package eEarn.com.userAuth.Services;

import eEarn.com.userAuth.Entity.ApplicationUser;
import eEarn.com.userAuth.Entity.RestPasswordRequest;
import eEarn.com.userAuth.Exceptions.NotFoundException;
import eEarn.com.userAuth.Repository.PasswordRestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class PasswordRestService {

    private final ApplicationUserService userService;

    private final PasswordRestRepository repository;

    private final PasswordEncoder passwordEncoder;
    public String RestRequestCheckUser(String username){
        ApplicationUser user = userService.checkAndGetApplicationUserByUsernameOrThrow(username);

        RestPasswordRequest request= RestPasswordRequest.builder()
                .restKey(UUID.randomUUID().toString())
                .applicationUser(user)
                .build();
        repository.save(request);
        return "we are send a mail check it and verify its you'r identity "+request.getRestKey();
    }

    public String RestUserPassword(String restKey, String password) {

        RestPasswordRequest request = getRequestedUserByRestKeyOrThrow(restKey);
        ApplicationUser user = request.getApplicationUser();
        user.setPassword(passwordEncoder.encode(password));
        userService.RegisterNewUser(user);


        return "password has been rest successfully";
    }

    private RestPasswordRequest getRequestedUserByRestKeyOrThrow(String restKey) {
        return repository.findByRestKey(restKey)
                .orElseThrow(() ->
                        new NotFoundException
                                ("Sorry we can't reset You'r password right now"));
    }
}
