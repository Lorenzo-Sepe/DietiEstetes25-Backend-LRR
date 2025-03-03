package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndirizzoRequest {

    @NotBlank(message = "La via è obbligatoria")
    private String via;

    @NotBlank(message = "Il numero civico è obbligatorio")
    private String numeroCivico;

    @NotBlank(message = "La città è obbligatoria")
    private String citta;

    @NotBlank(message = "Il CAP è obbligatorio")
    private String cap;

    @NotBlank(message = "La provincia è obbligatoria")
    private String provincia;

    @NotBlank(message = "La nazione è obbligatoria")
    private String nazione;

    @NotNull(message = "La latitudine è obbligatoria")
    private Double latitudine;

    @NotNull(message = "La longitudine è obbligatoria")
    private Double longitudine;
}
