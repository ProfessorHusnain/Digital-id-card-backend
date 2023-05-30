package eEarn.com.userAuth.Services;

import eEarn.com.userAuth.Dto.ApplicationRegistrationDto;
import eEarn.com.userAuth.Entity.ApplicationUser;
import eEarn.com.userAuth.Entity.EmailConfirmationToken;
import eEarn.com.userAuth.Exceptions.AlreadyExistException;
import eEarn.com.userAuth.Exceptions.EmptyArgumentException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


import static com.google.common.base.Strings.isNullOrEmpty;

import static eEarn.com.userAuth.Roles.ApplicationUserRole.BUSINESS;
import static eEarn.com.userAuth.Roles.ApplicationUserRole.USER;
import static eEarn.com.userAuth.Utilites.ApplicationConstants.EMPTY_ARGUMENT;

@Service
@Slf4j
@AllArgsConstructor
public class ApplicationRegistrationService {
    private final ApplicationUserService service;

    private final PasswordEncoder passwordEncoder;

    private final EmailConfirmationTokenService confirmationTokenService;

  //  private final KafkaProducerForNotification notification;


    public String RegisterNewUser(ApplicationRegistrationDto registrationDto) {
        System.out.println(registrationDto);
        checkFiledIsEmptyThenThrow(registrationDto);
        checkIfAlreadyExistThenThrow(registrationDto);

        if (registrationDto.getAccountType()==null) {
            ApplicationUser user = ApplicationUser.builder()
                    .firstName(registrationDto.getFirstName())
                    .lastName(registrationDto.getLastName())
                    .username(registrationDto.getUsername())
                    .userRole(USER)
                    .accountType(USER.name())
                    .email(registrationDto.getEmail())
                    .phoneNumber(registrationDto.getPhoneNumber())
                    .password(passwordEncoder.encode(registrationDto.getPassword()))
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isEnabled(true)
                    .isCredentialsNonExpired(true)
                    .build();
            service.RegisterNewUser(user);
            SendEmailForAccountVerification(user);
            return "Congratulation Your are Successfully Register !";
        }else {
            ApplicationUser user = ApplicationUser.builder()
                    .firstName(registrationDto.getFirstName())
                    .lastName(registrationDto.getLastName())
                    .username(registrationDto.getUsername())
                    .userRole(BUSINESS)
                    .accountType(registrationDto.getAccountType())
                    .email(registrationDto.getEmail())
                    .phoneNumber(registrationDto.getPhoneNumber())
                    .password(passwordEncoder.encode(registrationDto.getPassword()))
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isEnabled(true)
                    .isCredentialsNonExpired(true)
                    .build();
            service.RegisterNewUser(user);
            SendEmailForAccountVerification(user);
            return "Congratulation Your are Successfully Register !";
        }



    }
    private void SendEmailForAccountVerification(ApplicationUser user){

        EmailConfirmationToken emailConfirmationToken=EmailConfirmationToken
                .builder()
                .applicationUser(user)
                .token(UUID.randomUUID().toString())
                .CreatedAt(LocalDateTime.now())
                .ExpiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        confirmationTokenService.saveConfirmationToken(emailConfirmationToken);
       /* NotificationDto notificationDto=NotificationDto
                .builder()
                .ProducerType(ACCOUNT_VERIFICATION.getProducerType())
                .link("http://localhost:8084/user-auth/authentication/controller/account/verification?token="
                        +emailConfirmationToken.getToken())
                .build();
        notification.send(notificationDto);*/

    }
    public String confirmToken(String token)  {
        EmailConfirmationToken confirmationToken =
                confirmationTokenService.getToken(token);
        if (confirmationToken.getConfirmedAt() != null) {
            throw new AlreadyExistException("email Already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new AlreadyExistException("token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        service.enableUser(confirmationToken.getApplicationUser().getUsername());
        return "confirmed";
    }
    public char[] Hello(ApplicationRegistrationDto applicationRegistrationDto){
        return service.generateUniqueUsername(applicationRegistrationDto);
    }

    private void checkIfAlreadyExistThenThrow(ApplicationRegistrationDto registrationDto) {
        service.checkUserByEmailIfExistThenThrow(registrationDto.getEmail());
        service.checkUserByPhoneNumberIfExistThenThrow(registrationDto.getPhoneNumber());
        service.checkUserByUsernameIfExistThenThrow(registrationDto.getUsername());
    }

    private void checkFiledIsEmptyThenThrow(ApplicationRegistrationDto registrationDto) {
        if (isNullOrEmpty(registrationDto.getEmail())) {
            log.error(String.format(EMPTY_ARGUMENT, "email"));
            throw new EmptyArgumentException(String.format(EMPTY_ARGUMENT, "email"));
        }
        if (isNullOrEmpty(registrationDto.getUsername())) {
            log.error(String.format(EMPTY_ARGUMENT,"Username"));
            throw new EmptyArgumentException(String.format(EMPTY_ARGUMENT,"Username"));
        }
        if (isNullOrEmpty(registrationDto.getPhoneNumber())) {
            log.error(String.format(EMPTY_ARGUMENT, "PhoneNumber"));
            throw new EmptyArgumentException(String.format(EMPTY_ARGUMENT, "PhoneNumber"));
        }
        if (isNullOrEmpty(registrationDto.getPassword())) {
            log.error(String.format(EMPTY_ARGUMENT, "password"));
            throw new EmptyArgumentException(String.format(EMPTY_ARGUMENT, "password"));
        }
    }
}
