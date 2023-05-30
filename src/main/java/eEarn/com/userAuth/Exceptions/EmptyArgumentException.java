package eEarn.com.userAuth.Exceptions;

public class EmptyArgumentException extends RuntimeException{
    public EmptyArgumentException(String message) {
        super(message);
    }

    public EmptyArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
