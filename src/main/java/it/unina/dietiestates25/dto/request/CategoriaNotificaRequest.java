package it.unina.dietiestates25.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriaNotificaRequest {
    private boolean AttivoPromozioni;
    private boolean AttivoCategoriaPropostaAccettata;
    private boolean AttivoCategoriaPropostaRifitata;
    private boolean AttivoCategoriaControproposta;
    private boolean AttivoCategoriaOpportunitaImmobile;
    public boolean isAttivoPromozioni() {
        return AttivoPromozioni;
    }
    public boolean isAttivoCategoriaPropostaAccettata() {
        return AttivoCategoriaPropostaAccettata;
    }
    public boolean isAttivoCategoriaPropostaRifitata() {
        return AttivoCategoriaPropostaRifitata;
    }
    public boolean isAttivoCategoriaControproposta() {
        return AttivoCategoriaControproposta;
    }
    public boolean isAttivoCategoriaOpportunitaImmobile() {
        return AttivoCategoriaOpportunitaImmobile;
    }
}

