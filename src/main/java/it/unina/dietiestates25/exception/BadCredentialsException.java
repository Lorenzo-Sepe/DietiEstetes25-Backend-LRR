package it.unina.dietiestates25.exception;

import lombok.Getter;

@Getter
public class BadCredentialsException extends RuntimeException{

    private final String messageError;
    public BadCredentialsException(String messageError){
        super(String.format(messageError));
        this.messageError = messageError;
    }
}
