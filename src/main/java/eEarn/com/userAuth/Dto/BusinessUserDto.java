package eEarn.com.userAuth.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BusinessUserDto {
    private String consumerID;
    private String cardOwnerID;
    private String consumeType;
    private String productName;
    private String productPrice;

}
