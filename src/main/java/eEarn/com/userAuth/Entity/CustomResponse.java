package eEarn.com.userAuth.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@SuperBuilder
@AllArgsConstructor
@Data
public class CustomResponse {
    protected final Date time;
    protected final int statusCode;
    protected final HttpStatus status;
    protected String reason;
    protected final String message;
    protected final String developerMessage;
    protected final Map<?,?> Data;
}
