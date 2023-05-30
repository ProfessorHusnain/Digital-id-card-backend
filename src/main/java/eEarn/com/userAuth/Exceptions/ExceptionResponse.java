package eEarn.com.userAuth.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@JsonInclude(NON_NULL)
public class ExceptionResponse {

    protected final LocalDateTime time;
    protected final int status;
    protected final HttpStatus error;
    protected final String reason;
}
