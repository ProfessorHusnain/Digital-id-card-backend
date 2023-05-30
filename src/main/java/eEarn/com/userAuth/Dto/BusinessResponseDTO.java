package eEarn.com.userAuth.Dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BusinessResponseDTO {
    private String productName;
    private String productPrice;
    private String purchaserName;
    private String saleTime;
}
