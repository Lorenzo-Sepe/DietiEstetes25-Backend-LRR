package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Deprecated
public class NewAgentRequest {
    @NotBlank
    private String nome;
    @NotBlank
    private String cognome;
    @NotBlank(message = "Email cannot be null or blank")
    @Email(message = "Email not well-formed")
    String email;


}
