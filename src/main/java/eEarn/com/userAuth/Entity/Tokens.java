package eEarn.com.userAuth.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String authenticationToken;
    private String username;
    private String createdBy;
    private LocalDateTime createdOn;
    private Date ExpireOn;
    private String modifyBy;
    private LocalDateTime modifyOn;



}
