package eEarn.com.userAuth.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class EmailConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime CreatedAt;
    @Column(nullable = false)
    private LocalDateTime ExpiresAt;
    private LocalDateTime ConfirmedAt;
    @ManyToOne
    @JoinColumn(nullable = false
            ,name = "application_user_id")
    private ApplicationUser applicationUser;
}
