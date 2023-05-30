package eEarn.com.userAuth.Security;

import eEarn.com.userAuth.Jwt.JwtConfiguration;
import eEarn.com.userAuth.Jwt.JwtSecretKey;
import eEarn.com.userAuth.Jwt.JwtTokenVerifier;
import eEarn.com.userAuth.Jwt.JwtUsernameAndPasswordAuthenticationFilter;
import eEarn.com.userAuth.Services.ApplicationUserService;
import eEarn.com.userAuth.Services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ApplicationUserService userService;
    private final PasswordEncoder passwordEncoder;

    private final JwtConfiguration jwtConfiguration;

    private final JwtSecretKey jwtSecretKey;
    private final SecurityEntryPoint securityEntryPoint;

    private final TokenService tokenService;

private final CrossFilterConfiguration crossFilterConfiguration;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
               .csrf().disable()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .cors()
               .and()
               .exceptionHandling().authenticationEntryPoint(securityEntryPoint)
               .and()
               .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager()
                       ,jwtConfiguration,jwtSecretKey,tokenService))
               .addFilterAfter(new JwtTokenVerifier(jwtConfiguration,jwtSecretKey,tokenService)
                       ,JwtUsernameAndPasswordAuthenticationFilter.class)
               .authorizeRequests()
               .antMatchers("/index","/css/*","/js/*","/authentication/controller/**","/login","/Hello/**")
               .permitAll()
               .anyRequest()
               .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }


    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        //corsConfiguration.setAllowedOrigins(Arrays.asList("${Angular.URL}"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin",
                "Content-Type", "Accept","JwtToken", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Access-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin",
                "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
