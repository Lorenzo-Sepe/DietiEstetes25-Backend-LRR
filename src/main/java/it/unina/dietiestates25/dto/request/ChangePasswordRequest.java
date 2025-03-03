package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    String oldPassword;
    @NotBlank(message = "La password non può essere nulla o vuota")
    @Size(min = 8, max = 16, message = "La password deve essere compresa tra 8 e 16 caratteri")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$£%^&+=!]).*$",
            message = "La password deve contenere almeno un numero, una lettera minuscola, una lettera maiuscola e un carattere speciale")
    String newPassword;
    @NotBlank
    String confirmPassword;
}
