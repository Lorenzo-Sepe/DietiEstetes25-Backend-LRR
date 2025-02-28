package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewAgentRequest {
    @NotBlank
    private String nome;
    @NotBlank
    private String cognome;
    @NotBlank(message = "Username cannot be null or blank")
    @Size(max = 20, min = 3, message = "Username must be between 3 and 20 characters")
    String username;
    @NotBlank(message = "Email cannot be null or blank")
    @Email(message = "Email not well-formed")
    String email;


}
