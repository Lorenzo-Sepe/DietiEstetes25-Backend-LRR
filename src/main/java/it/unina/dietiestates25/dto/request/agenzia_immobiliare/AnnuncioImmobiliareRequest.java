package it.unina.dietiestates25.dto.request.agenzia_immobiliare;

import it.unina.dietiestates25.dto.request.ImmobileRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnuncioImmobiliareRequest {

    @NotBlank(message = "Il titolo è obbligatorio")
    @Size(min = 1, max = 255, message = "Il titolo deve avere tra 1 e 255 caratteri")
    private String titolo;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Size(min = 1, max = 1000, message = "La descrizione deve avere tra 1 e 1000 caratteri")
    private String descrizione;

    @NotNull(message = "L'immobile è obbligatorio")
    private ImmobileRequest immobile;

    @NotNull(message = "Il contratto è obbligatorio")
    private ContrattoRequest contratto;

}