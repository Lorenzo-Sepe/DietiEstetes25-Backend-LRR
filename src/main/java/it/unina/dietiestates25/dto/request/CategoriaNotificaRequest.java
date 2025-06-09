package it.unina.dietiestates25.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class CategoriaNotificaRequest {
    private boolean attivoPromozioni;
    private boolean attivoCategoriaPropostaAccettata;
    private boolean attivoCategoriaPropostaRifiutata;
    private boolean attivoCategoriaControproposta;
    private boolean attivoCategoriaOpportunityImmobile;
}

