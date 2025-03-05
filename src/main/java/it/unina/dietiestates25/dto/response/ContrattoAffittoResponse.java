package it.unina.dietiestates25.dto.response;

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
}
