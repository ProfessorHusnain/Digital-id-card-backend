package eEarn.com.userAuth.Resource;

import eEarn.com.userAuth.Dto.ApplicationRegistrationDto;
import eEarn.com.userAuth.Entity.CustomResponse;
import eEarn.com.userAuth.Security.JwtTokenValidateService;
import eEarn.com.userAuth.Services.ApplicationRegistrationService;
import eEarn.com.userAuth.Services.PasswordRestService;
import eEarn.com.userAuth.Services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.of;

@RestController
@RequestMapping("/authentication/controller")
@AllArgsConstructor
public class ServiceController {

    private final ApplicationRegistrationService registrationService;

    private final JwtTokenValidateService jwtTokenValidateService;

    private final PasswordRestService restService;
    private final TokenService tokenService;


    @PostMapping("validateToken")
    public ResponseEntity<String> tokenValidateForAccessingServices(@RequestParam("token") String token) {
        return ResponseEntity.ok(
                jwtTokenValidateService.ValidateToken(token)
        );
    }

    @PostMapping("Register")
    public ResponseEntity<CustomResponse> tokenValidateForAccessingServices(@RequestBody ApplicationRegistrationDto registrationDto) {
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .message("ok")
                        .Data(of("message", registrationService.RegisterNewUser(registrationDto)))
                        .build()
        );
    }
    @PostMapping(path = "logout")
    public ResponseEntity<CustomResponse> Logout(@Param("id") String id) {

        Map<String, String> mess = new HashMap<>();
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .Data(Map.of("message",tokenService.logout(id)))
                        .build()
        );
    }

    @PostMapping("RestPasswordRequest")
    public ResponseEntity<CustomResponse> checkUserExistForRestPassword(@RequestParam("username") String username){
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .Data(of("message",restService.RestRequestCheckUser(username)))
                        .build()
        );
    }

    @PostMapping("RestPassword")
    public ResponseEntity<CustomResponse> restPassword(@RequestParam("restKey") String restKey,@RequestParam("password") String password){
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .Data(of("message",restService.RestUserPassword(restKey,password)))
                        .build()
        );
    }
    @PostMapping("topic")
    public ResponseEntity<CustomResponse> tokenValidaorAccessingServices(@RequestBody ApplicationRegistrationDto registrationDto) {
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .message("ok")
                       // .Data(of("Registration Response =", notification.send(registrationDto)))
                        .build()
        );
    }
    @GetMapping("account/verification")
    public ResponseEntity<CustomResponse> AccountVerificationByEmail(@RequestParam("token") String token) {
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .message("ok")
                        .Data(of("Confirmation : ",registrationService.confirmToken(token)))
                        .build()
        );
    }

}
