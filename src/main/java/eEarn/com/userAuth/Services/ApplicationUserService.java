package eEarn.com.userAuth.Services;

import eEarn.com.userAuth.Dto.ApplicationRegistrationDto;
import eEarn.com.userAuth.Dto.ApplicationUserDTO;
import eEarn.com.userAuth.Entity.ApplicationUser;
import eEarn.com.userAuth.Entity.Tokens;
import eEarn.com.userAuth.Exceptions.AlreadyExistException;
import eEarn.com.userAuth.Repository.ApplicationUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static eEarn.com.userAuth.Utilites.ApplicationConstants.*;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class ApplicationUserService implements UserDetailsService {


    private final ApplicationUserRepository userRepository;

    private final TokenService tokenService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getApplicationUserByUsernameOrThrow(username);
    }

    private ApplicationUser getApplicationUserByUsernameOrThrow(String username) {
        return userRepository.findApplicationUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE)));
    }

    public ApplicationUser getApplicationUserByIdOrThrow(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE)));
    }



    public ApplicationUser checkAndGetApplicationUserByUsernameOrThrow(String username) {
        return getApplicationUserByUsernameOrThrow(username);
    }



    public void checkUserByPhoneNumberIfExistThenThrow(String phoneNumber) {
        Optional<ApplicationUser> applicationUserByPhoneNumber = userRepository.findApplicationUserByPhoneNumber(phoneNumber);
        if (applicationUserByPhoneNumber.isPresent()) {
            log.error(String.format("%s " + PHONE_NUMBER_EXIST_MESSAGE, phoneNumber));
            throw new AlreadyExistException(String.format("%s " + PHONE_NUMBER_EXIST_MESSAGE, phoneNumber));
        }
    }

    public void checkUserByEmailIfExistThenThrow(String email) {
        Optional<ApplicationUser> applicationUserByEmail = userRepository.findApplicationUserByEmail(email);
        if (applicationUserByEmail.isPresent()) {
            log.error(String.format("%s " + EMAIL_EXIST_MESSAGE, email));
            throw new AlreadyExistException(String.format("%s " + EMAIL_EXIST_MESSAGE, email));
        }
    }

    public void checkUserByUsernameIfExistThenThrow(String username) {
        Optional<ApplicationUser> applicationUserByUsername = userRepository.findApplicationUserByUsername(username);
        if (applicationUserByUsername.isPresent()) {
            log.error(String.format("%s " + USER_FOUND_MESSAGE, username));
            throw new AlreadyExistException(String.format("%s " + USER_FOUND_MESSAGE, username));
        }
    }

    public void RegisterNewUser(ApplicationUser user) {
        userRepository.save(user);
    }

    public char[] generateUniqueUsername(ApplicationRegistrationDto obj) {
        String[] split = obj.getEmail().split("@");
        String usernameRequest = obj.getFirstName() + obj.getLastName() + split[0];
        char[] username = new char[obj.getFirstName().length()];
        for (int i = 0; i < obj.getFirstName().length(); i++) {
            username[i] = usernameRequest.charAt(new Random().nextInt(usernameRequest.length()));
        }
        return username;
    }

    public void enableUser(String username) {
        ApplicationUser user = getApplicationUserByUsernameOrThrow(username);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public List<ApplicationUserDTO> getAllUsers(String id) {
        Optional<Tokens> token = tokenService.getToken(id);
        ApplicationUser applicationUserByUsernameOrThrow = getApplicationUserByUsernameOrThrow(token.get().getUsername());
        List<ApplicationUserDTO> collect = userRepository.findAll().stream().filter(m -> m.getId() != applicationUserByUsernameOrThrow.getId())
                .map(m -> ApplicationUserDTO
                        .builder()
                        .firstName(m.getFirstName())
                        .lastName(m.getLastName())
                        .AccountType(m.getUserRole().name())
                        .phoneNumber(m.getPhoneNumber())
                        .username(m.getUsername())
                        .email(m.getEmail())
                        .build()).collect(Collectors.toList());
        return collect;

    }
}
