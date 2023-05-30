package eEarn.com.userAuth.Exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Object> unAuthorizedException(UnAuthorizedException e){
        return new ResponseEntity<>(
                ExceptionResponse
                        .builder()
                        .time(LocalDateTime.now())
                        .status(UNAUTHORIZED.value())
                        .error(UNAUTHORIZED)
                        .reason(e.getMessage())
                        .build()
                ,UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> NotFoundException(NotFoundException e){
        return new ResponseEntity<>(
                ExceptionResponse
                        .builder()
                        .time(LocalDateTime.now())
                        .status(NOT_FOUND.value())
                        .error(NOT_FOUND)
                        .reason(e.getMessage())
                        .build()
                ,NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> UsernameNotFoundException(UsernameNotFoundException e){
        return new ResponseEntity<>(
                ExceptionResponse
                        .builder()
                        .time(LocalDateTime.now())
                        .status(NOT_FOUND.value())
                        .error(NOT_FOUND)
                        .reason(e.getMessage())
                        .build()
                ,NOT_FOUND);
    }
    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Object> AlreadyExistException(AlreadyExistException e){
        return new ResponseEntity<>(
                ExceptionResponse
                        .builder()
                        .time(LocalDateTime.now())
                        .status(CONFLICT.value())
                        .error(CONFLICT)
                        .reason(e.getMessage())
                        .build()
                ,CONFLICT);
    }

    @ExceptionHandler(EmptyArgumentException.class)
    public ResponseEntity<Object> EmptyArgumentException(EmptyArgumentException e){
        return new ResponseEntity<>(
                ExceptionResponse
                        .builder()
                        .time(LocalDateTime.now())
                        .status(NO_CONTENT.value())
                        .error(NO_CONTENT)
                        .reason(e.getLocalizedMessage())
                        .build()
                ,NOT_ACCEPTABLE);
    }
}
