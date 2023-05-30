package eEarn.com.userAuth.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@JsonInclude(NON_NULL)
public class NotificationDto {
    private String link;
    private String ProducerType;
    private String code;
    private String Message;
}
