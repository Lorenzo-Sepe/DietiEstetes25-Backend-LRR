package it.unina.dietiestates25.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{

    private final String messageError;

    public BadRequestException(String messageError) {
        super(String.format(messageError));
        this.messageError = messageError;
    }
}
