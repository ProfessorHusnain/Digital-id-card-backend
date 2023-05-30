package eEarn.com.userAuth.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String globalCardId;
    private LocalDateTime createdOn;
    private LocalDateTime expireOn;
    private Boolean isActive;
    private String cardType;
    private Long ownerId;
    private Long visitorId;
   /* @ManyToOne(optional = true,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "ownerId",referencedColumnName = "Id")
    private ApplicationUser ownerId;

    @ManyToOne(optional = true,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "visitorId",referencedColumnName = "Id")
    private ApplicationUser  visitorId;
*/
    private Long productId;
}
