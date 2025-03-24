package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.ContrattoAffitto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContrattoAffittoResponse {

    private Double prezzoAffitto;
    private int tempoMinimo;
    private int tempoMassimo;
    private Double caparra;

    public static ContrattoAffittoResponse fromEntityToDto(ContrattoAffitto contratto){

        return ContrattoAffittoResponse.builder()
                .caparra(contratto.getCaparra())
                .prezzoAffitto(contratto.getPrezzoAffitto())
                .tempoMinimo( contratto.getTempoMinimo())
                .tempoMassimo(contratto.getTempoMassimo())
                .build();
    }
}
