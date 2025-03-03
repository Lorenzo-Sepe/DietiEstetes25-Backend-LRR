package it.unina.dietiestates25.dto.request.agenziaImmobiliare;

import it.unina.dietiestates25.entity.ContrattoAffitto;
import it.unina.dietiestates25.entity.ContrattoVendita;
import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContrattoRequest {

    @NotNull(message = "Il prezzo è obbligatorio")
    @Min(value = 0, message = "Il prezzo non può essere negativo")
    private Double prezzo;

    private DatiAffittoRequest datiAffittoRequest;

    private DatiVenditaRequest datiVenditaRequest;

}
