package eEarn.com.userAuth.Resource;

import eEarn.com.userAuth.Entity.CustomResponse;
import eEarn.com.userAuth.Entity.GlobalCard;
import eEarn.com.userAuth.Repository.ApplicationUserRepository;
import eEarn.com.userAuth.Repository.GlobalCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("Hello")
@AllArgsConstructor
public class Text {
    private final ApplicationUserRepository repository;
    private final GlobalCardRepository globalCardRepository;
    @GetMapping
    public ResponseEntity<CustomResponse> uiuiouiuiuiij(@Param("id") Long id){
       return ResponseEntity.ok(
          CustomResponse.builder().Data(Map.of("Hell",repository.findById(id)))
                  .build()
        );
    }
    @GetMapping("all")
    public ResponseEntity<CustomResponse> uiouiuiuiij(@Param("id") Long id){
        return ResponseEntity.ok(
                CustomResponse.builder().Data(Map.of("Hell",globalCardRepository.findById(id)))
                        .build()
        );
    }
}
