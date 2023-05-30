package eEarn.com.userAuth.Jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import eEarn.com.userAuth.Entity.ApplicationUser;
import eEarn.com.userAuth.Entity.JwtUsernameAndPasswordAuthenticationRequest;
import eEarn.com.userAuth.Entity.Tokens;
import eEarn.com.userAuth.Roles.ApplicationUserRole;
import eEarn.com.userAuth.Services.TokenService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.security.auth.login.AccountLockedException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private final AuthenticationManager authenticationManager;

    private final JwtConfiguration jwtConfiguration;

    private final JwtSecretKey jwtSecretKey;

    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        JwtUsernameAndPasswordAuthenticationRequest
                authenticationRequest = getJwtUsernameAndPasswordAuthenticationRequest(request);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        return authenticationManager.authenticate(authentication);

    }

    private static JwtUsernameAndPasswordAuthenticationRequest getJwtUsernameAndPasswordAuthenticationRequest(HttpServletRequest request) {
        JwtUsernameAndPasswordAuthenticationRequest authenticationRequest;
        try {
            authenticationRequest = new ObjectMapper().readValue(request.getInputStream(),
                    JwtUsernameAndPasswordAuthenticationRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authenticationRequest;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException,
            ServletException {



        ApplicationUser user = (ApplicationUser) authResult.getPrincipal();

        tokenService.CheckIsPresentThenDeleteIt(user.getUsername());

        Tokens token= Tokens.builder()
                 .authenticationToken(UUID.randomUUID().toString())
                .username(user.getUsername())
                //.createdBy("SYSTEM")
                .createdOn(LocalDateTime.now())
                //.modifyBy("SYSTEM")
                //.modifyOn(LocalDateTime.now())
                .ExpireOn(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfiguration
                        .getTokenExpirationAfterDays())))
                .build();

        String access_token = Jwts.builder()
                .setSubject(token.getAuthenticationToken())
                //.setIssuer("eEarn")
                .claim("authorities", authResult.getAuthorities())
                //.claim("Role",user.getRole())
                .setIssuedAt(new Date())
                //.setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 1000))
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfiguration
                        .getTokenExpirationAfterDays())))
                //.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                //.signWith(Keys.hmacShaKeyFor("keyhhhhhhhhhhhhhhhhhhhhhskjskhdkh".getBytes()))
                .signWith(jwtSecretKey.SecretKey())
                .compact();


        tokenService.saveToken(token);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token",
                String.format("%s %s",
                        jwtConfiguration.getTokenPrefix()
                        ,access_token));
        tokens.put("ROLE",user.getUserRole().name().toString());
         tokens.put("Expire",token.getExpireOn().toString());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        Class<? extends AuthenticationException> aClass = failed.getClass();
        Map<String, String> error = new HashMap<>();
        if (aClass.equals(DisabledException.class)) {
            response.setStatus(FORBIDDEN.value());
            error.put("massage", String.format("Dear User You're currently disable"));
        }else if (aClass.equals(BadCredentialsException.class)) {
            response.setStatus(FORBIDDEN.value());
            error.put("massage", String.format("Dear User Your username or password is un correct"));
        }else if (aClass.equals(AccountExpiredException.class)) {
            response.setStatus(FORBIDDEN.value());
            error.put("massage", String.format("Dear User Your account is Expired"));
        }else if (aClass.equals(LockedException.class)) {
            response.setStatus(FORBIDDEN.value());
            error.put("massage", String.format("Dear User Your account is locked"));
        }else if (aClass.equals(CredentialsExpiredException.class)) {
            response.setStatus(FORBIDDEN.value());
            error.put("massage", String.format("Dear User Your session has been expired login again"));
        }

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);

    }


}
