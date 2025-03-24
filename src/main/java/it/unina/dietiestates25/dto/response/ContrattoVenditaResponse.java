package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.ContrattoVendita;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContrattoVenditaResponse {

    private Double prezzoVendita;
    private boolean mutuoEstinto;
    
    public static ContrattoVenditaResponse getContrattoVendita(ContrattoVendita contratto){
        return ContrattoVenditaResponse.builder()
                .mutuoEstinto(contratto.isMutuoEstinto())
                .prezzoVendita(contratto.getPrezzoVendita())
                .build();
    }
}
