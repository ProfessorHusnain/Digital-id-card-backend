package eEarn.com.userAuth.Services;


import eEarn.com.userAuth.Entity.EmailConfirmationToken;
import eEarn.com.userAuth.Exceptions.NotFoundException;
import eEarn.com.userAuth.Repository.EmailConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailConfirmationTokenService {
    private final EmailConfirmationTokenRepository tokenRepository;

    public void saveConfirmationToken(EmailConfirmationToken token){
        tokenRepository.save(token);
    }

    public EmailConfirmationToken getToken(String token){
        return tokenRepository.findByToken(token)
                .orElseThrow(()-> new
                        NotFoundException("Token does not exist please request for Re-Generate"));
    }

    public void setConfirmedAt(String token){
        Optional<EmailConfirmationToken> byToken = tokenRepository.findByToken(token);
        byToken.get().setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(byToken.get());
    }
}
