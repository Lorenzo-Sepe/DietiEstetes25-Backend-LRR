package it.unina.dietiestates25.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatiAffittoRequest {

    private int tempoMinimo;

    private int tempoMassimo;

    @NotNull(message = "Il prezzo è obbligatorio")
    @Min(value = 0, message = "Il prezzo non può essere negativo")
    private Double prezzo;

    private Double caparra;
}
