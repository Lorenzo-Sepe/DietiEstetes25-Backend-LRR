package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgenziaImmobiliareRequest {

    @NotBlank
    String nomeAgenzia;

    @NotBlank
    String ragioneSociale;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$")
    String partitaIva;

    //non deve contenere caratteri speciali o spazi
    @Pattern(regexp = "^[a-z0-9]+$")
    @NotBlank
    String dominio;

    //Dati Fondatore
    @NotBlank
    String nomeFondatore;
    @NotBlank
    String cognomeFondatore;
    @Email
    String emailFondatore;

}
