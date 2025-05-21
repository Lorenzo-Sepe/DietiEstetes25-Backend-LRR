package it.unina.dietiestates25.dto.request;

import it.unina.dietiestates25.entity.AnnuncioImmobiliare;
import it.unina.dietiestates25.entity.ContrattoAffitto;
import it.unina.dietiestates25.entity.ContrattoVendita;
import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
public class CriteriDiRicercaUtenti {
    @NotBlank
    private String areaDiInteresse;

    private TipoContratto tipoDiContrattoDiInteresse;
    private TipologiaImmobile tipologiaDiImmobileDiInteresse;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private int intervalloGiorniStoricoRicerca;


    public static CriteriDiRicercaUtenti map(AnnuncioImmobiliare annuncio) {
        TipoContratto tipoContratto  = null;
        Double prezzo=null;
        if(annuncio.getContratto() instanceof ContrattoAffitto){
            tipoContratto= TipoContratto.AFFITTO;
            prezzo = ((ContrattoAffitto) annuncio.getContratto()).getPrezzoAffitto();
        }
        else if(annuncio.getContratto() instanceof ContrattoAffitto){
            tipoContratto= TipoContratto.VENDITA;
            prezzo= ((ContrattoVendita) annuncio.getContratto()).getPrezzoVendita();
        }

        BigDecimal budgetMin = null;
        BigDecimal budgetMax = null;
        if (prezzo != null) {
            budgetMin = BigDecimal.valueOf(0);
            budgetMax = BigDecimal.valueOf(prezzo * 1.1);
        }
        return CriteriDiRicercaUtenti.builder()
                .areaDiInteresse(annuncio.getImmobile().getIndirizzo().getCitta())
                .tipoDiContrattoDiInteresse(tipoContratto)
                .tipologiaDiImmobileDiInteresse(annuncio.getImmobile().getTipologiaImmobile())
                .budgetMin(budgetMin)
                .budgetMax(budgetMax)
                .intervalloGiorniStoricoRicerca(30)
                .build();
    }
}