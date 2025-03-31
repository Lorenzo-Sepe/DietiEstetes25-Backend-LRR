package it.unina.dietiestates25.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{

    private final String resourceName;
    private final String fieldName;
    private final transient Object value;

    public ResourceNotFoundException(String resourceName, String fieldName, Object value) {
        super(String.format("%s non trovato con il seguente %s: %s", resourceName, fieldName, value));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.value = value;
    }
}
