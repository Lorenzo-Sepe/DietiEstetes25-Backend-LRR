package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(

        @NotBlank(message = "Il nome utente non può essere nullo o vuoto")
        @Size(max = 20, min = 3, message = "Il nome utente deve essere compreso tra 3 e 20 caratteri")
        String username,

        @NotBlank(message = "L'email non può essere nulla o vuota")
        @Email(message = "Formato email non valido")
        String email,

        @NotBlank(message = "La password non può essere nulla o vuota")
        @Size(min = 8, max = 16, message = "La password deve essere compresa tra 8 e 16 caratteri")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$£%^&+=!]).*$",
                message = "La password deve contenere almeno un numero, una lettera minuscola, una lettera maiuscola e un carattere speciale")
        String password
) {
}
