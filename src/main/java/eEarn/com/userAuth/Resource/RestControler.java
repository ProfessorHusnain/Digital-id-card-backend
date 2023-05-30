package eEarn.com.userAuth.Resource;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import eEarn.com.userAuth.Dto.BusinessUserDto;
import eEarn.com.userAuth.Entity.BusinessHolder;
import eEarn.com.userAuth.Entity.CustomResponse;
import eEarn.com.userAuth.Services.ApplicationUserService;
import eEarn.com.userAuth.Services.GlobalCardService;
import eEarn.com.userAuth.Services.QRCodeService;
import eEarn.com.userAuth.Services.TokenService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("DigitalCard")
@AllArgsConstructor
public class RestControler {

    private final GlobalCardService qrCodeService;
    private final ApplicationUserService userService;


    @GetMapping(path = "ProfileAtHome")
   public  ResponseEntity<CustomResponse> getProfileWithCardDitails(@Param("id") String id) {
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .Data(Map.of("message",qrCodeService.getProfile(id)))
                        .build()
        );
    }
    @PostMapping(path = "Identity")
    public ResponseEntity<CustomResponse> getIdentity(@RequestBody BusinessUserDto userDto) {
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .Data(Map.of("message",qrCodeService.updateGlobalCard(userDto)))
                        .build()
        );
    }

    @GetMapping(path = "Profile")
    public ResponseEntity<CustomResponse> getProfile(@Param("id") String id) {
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .Data(Map.of("Profile",qrCodeService.getApplicationUserProfile(id)))
                        .build()
        );
    }
    @GetMapping(path = "BusinessDetails")
    public ResponseEntity<CustomResponse> getBusinessDetails(@Param("id") String id) {
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .Data(Map.of("Profile",qrCodeService.getBusinessDetails(id)))
                        .build()
        );
    }
    @GetMapping(path = "Users")
    public ResponseEntity<CustomResponse> getAppUsers(@Param("id") String id) {
        return ResponseEntity.ok(
                CustomResponse.builder()
                        .Data(Map.of("Profile",userService.getAllUsers(id)))
                        .build()
        );
    }


}
