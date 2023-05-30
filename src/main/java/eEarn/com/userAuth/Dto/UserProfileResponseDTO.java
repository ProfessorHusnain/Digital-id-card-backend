package eEarn.com.userAuth.Dto;

import eEarn.com.userAuth.Entity.GlobalCard;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserProfileResponseDTO {
    private String username;
    private String card;
    private List<GlobalCardDTO> globalCardDTOList;
}
