package it.unina.dietiestates25.dto.request.agenzia_immobiliare;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgenziaImmobiliareRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    String nomeAgenzia;
    @NotBlank
    @Size(min = 3, max = 80)
    String ragioneSociale;
    @NotBlank
    @Pattern(regexp = "^\\d{11}$")
    String partitaIva;
    // non deve contenere caratteri speciali o spazi
    @Pattern(regexp = "^[a-z0-9]+$")
    @NotBlank
    @Size(min = 3, max = 20)
    String dominio;

    // Dati Fondatore
    @NotBlank
    @Size(min = 3, max = 100)
    String nomeFondatore;
    @NotBlank
    @Size(min = 3, max = 100)
    String cognomeFondatore;
    @Email
    String emailFondatore;

}
