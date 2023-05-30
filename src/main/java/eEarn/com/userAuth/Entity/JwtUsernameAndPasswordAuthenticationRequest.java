package eEarn.com.userAuth.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtUsernameAndPasswordAuthenticationRequest {
    private String username;
    private String password;
}
