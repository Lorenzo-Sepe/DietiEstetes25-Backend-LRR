package it.unina.dietiestates25.dto.request.agenziaImmobiliare;

import it.unina.dietiestates25.dto.request.DatiAffittoRequest;
import it.unina.dietiestates25.dto.request.DatiVenditaRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContrattoRequest {

    @NotNull(message = "Il tipo di contratto + obbligatorio")
    private String tipoDiContratto;

    private DatiAffittoRequest datiAffittoRequest;

    private DatiVenditaRequest datiVenditaRequest;

}
