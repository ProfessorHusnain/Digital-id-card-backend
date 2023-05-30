package eEarn.com.userAuth.Security;

import com.google.common.base.Strings;
import eEarn.com.userAuth.Exceptions.UnAuthorizedException;
import eEarn.com.userAuth.Jwt.JwtConfiguration;
import eEarn.com.userAuth.Jwt.JwtSecretKey;
import eEarn.com.userAuth.Services.ApplicationUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class JwtTokenValidateService {

    private final JwtConfiguration jwtConfiguration;
    private final JwtSecretKey jwtSecretKey;
    private final static String UN_AUTHORIZED_MESSAGE="Your un authorized";

    private final ApplicationUserService userService;


    public String ValidateToken(String Token){
        log.info("ValidateToken");

        if (Strings.isNullOrEmpty(Token) || !Token.startsWith(jwtConfiguration.getTokenPrefix())) {
            throw new UnAuthorizedException(UN_AUTHORIZED_MESSAGE);
        }

        String originalTokenGetFromRequest = Token.replace(jwtConfiguration.getTokenPrefix(), "");


            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(jwtSecretKey.SecretKey())
                    .parseClaimsJws(originalTokenGetFromRequest);
            Claims body = claimsJws.getBody();

            if (body.getExpiration().before(new Date())){
                throw new UnAuthorizedException("Please Login again session has been expired");
            }
        String username = body.getSubject();
        var auth = (List<Map<String, String>>) body.get("authorities");

        Set<SimpleGrantedAuthority> authority = auth.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                .collect(Collectors.toSet());

        /* Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                authority
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);*/


            return userService.
                    checkAndGetApplicationUserByUsernameOrThrow(username)
                    .getId()
                    .toString();
    }

}
