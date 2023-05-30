package eEarn.com.userAuth.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GlobalCardDTO {
    private String consumeType;
    private LocalDateTime consumeOn;
    private LocalDateTime createOn;
    private String cardConsumer;
}
