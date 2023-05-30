package eEarn.com.userAuth.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import eEarn.com.userAuth.Entity.Tokens;
import eEarn.com.userAuth.Exceptions.ExceptionResponse;
import eEarn.com.userAuth.Exceptions.UnAuthorizedException;
import eEarn.com.userAuth.Services.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfiguration jwtConfiguration;
    private final JwtSecretKey jwtSecretKey;

    private final TokenService tokenService;

    @Override

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException,
            IOException {

        String Token = request.getHeader(jwtConfiguration.getAuthorizationHeader());
        if (Strings.isNullOrEmpty(Token) || !Token.startsWith(jwtConfiguration.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String originalTokenGetFromRequest = getBearerFreeTokenFromRequest(Token);

        try {

            Claims body = getTokenBody(originalTokenGetFromRequest);

            Optional<Tokens> token = getToken(body);

            var auth = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> authority = auth.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    token.get().getUsername(),
                    null,
                    authority
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {

            log.error(e.getLocalizedMessage());

            response.setHeader("error",e.getLocalizedMessage());
            response.setStatus(FORBIDDEN.value());
            //throw new IllegalStateException(String.format("Token %s Is not trusted", originalTokenGetFromRequest));
            Map<String,String> error = new HashMap<>();
            error.put("massage",e.getLocalizedMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        } catch (UnAuthorizedException e){

            log.error(e.getLocalizedMessage());

            response.setStatus(UNAUTHORIZED.value());
            //throw new IllegalStateException(String.format("Token %s Is not trusted", originalTokenGetFromRequest));
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),
                    ExceptionResponse.builder()
                            //.time(LocalDateTime.now())
                            .status(UNAUTHORIZED.value())
                            .error(UNAUTHORIZED)
                            .reason(e.getLocalizedMessage())
                            .build());
        }


        filterChain.doFilter(request, response);
    }

    public String getBearerFreeTokenFromRequest(String Token) {
        String originalTokenGetFromRequest = Token.replace(jwtConfiguration.getTokenPrefix(), "");
        return originalTokenGetFromRequest;
    }

    public Claims getTokenBody(String originalTokenGetFromRequest) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(jwtSecretKey.SecretKey())
                .parseClaimsJws(originalTokenGetFromRequest);
        Claims body = claimsJws.getBody();
        return body;
    }

    public Optional<Tokens> getToken(Claims body) {
        String tokenId = body.getSubject();
        Optional<Tokens> token = tokenService.getToken(tokenId);
        if (body.getExpiration().before(new Date())){

            tokenService.deleteById(token.get().getId());
            throw new UnAuthorizedException("Please Login again session has been expired");
        }
        if (!token.isPresent()){
            throw new UnAuthorizedException("Token Not Found");
        }
        return token;
    }


}
