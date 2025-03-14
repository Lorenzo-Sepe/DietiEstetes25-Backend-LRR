package it.unina.dietiestates25.dto.request;

import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NotificaPromozionaleRequest {

    @NotBlank
    private String contenuto;

    //TODO INCAPSULARE IN UN ALTRA CLASSE
    @NotBlank
    private String areaDiInteresse;

    private TipoContratto tipoDiContrattoDiInteresse;

    private TipologiaImmobile tipologiaDiImmobileDiInteresse;

    private BigDecimal budgetMin;

    private BigDecimal budgetMax;

    private int IntervallogiorniStoricoRicerca;
}
