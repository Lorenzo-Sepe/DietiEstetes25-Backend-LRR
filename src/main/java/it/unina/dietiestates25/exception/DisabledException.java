package it.unina.dietiestates25.exception;

import lombok.Getter;

@Getter
public class DisabledException extends RuntimeException{

    private final String messageError;
    public DisabledException(String messageError){
        super(String.format(messageError));
        this.messageError = messageError;
    }
}
