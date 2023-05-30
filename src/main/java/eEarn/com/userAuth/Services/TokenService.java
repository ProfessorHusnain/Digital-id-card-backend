package eEarn.com.userAuth.Services;

import eEarn.com.userAuth.Entity.Tokens;
import eEarn.com.userAuth.Repository.TokensRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class TokenService {
    private final TokensRepository repository;

    public void saveToken(Tokens tokens){

        repository.save(tokens);

        log.info(String
                .format("%s token is issued and saved at %s!"
                        ,tokens.getUsername()
                        ,tokens.getCreatedOn()
                                .toLocalTime()));
    }

    public void CheckIsPresentThenDeleteIt(String username){
        Optional<Tokens> byUsername = repository.findByUsername(username);
        if (byUsername.isPresent()){
            repository.deleteById(byUsername.get().getId());
        }
    }

    public Optional<Tokens> getToken(String token){
        return repository.findByAuthenticationToken(token);
    }

    public String logout(String token){
        Optional<Tokens> byAuthenticationToken = repository.findByAuthenticationToken(token);
        repository.deleteById(byAuthenticationToken.get().getId());
        return "You'r logout successfully";
    }

    public void validateToken(){

    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
