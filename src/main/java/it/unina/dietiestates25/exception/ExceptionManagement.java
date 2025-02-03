package it.unina.dietiestates25.exception;

import it.unina.dietiestates25.utils.Msg;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionManagement {

    @ExceptionHandler({InternalServerErrorException.class})
    public ResponseEntity<String> internalServerErrorExceptionManagement(InternalServerErrorException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> illegalArgumentExceptionManagement(IllegalArgumentException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<String> conflictExceptionManagement(ConflictException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> resourceNotFoundExceptionManagement(ResourceNotFoundException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DisabledException.class})
    public ResponseEntity<String> disabledExceptionManagement(DisabledException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<String> accessDeniedExceptionManagement(AccessDeniedException ex){
        return new ResponseEntity<String>(Msg.ACCESS_DENIED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<String> badCredentialsExceptionManagement(BadCredentialsException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN); // 403
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> badRequestExceptionManagement(BadRequestException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<String> unauthorizedExceptionManagement(UnauthorizedException ex){
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);  // 401
    }

    // Per la gestione delle eccezioni sollevate dalle annotazioni di validazione con @Validated
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> constraintViolationExceptionManagement(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> {
                            String path = violation.getPropertyPath().toString();
                            return path.substring(path.lastIndexOf('.') + 1);
                            },
                        violation -> violation.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Per la gestione delle eccezioni sollevate dalle annotazioni di validazione con @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
