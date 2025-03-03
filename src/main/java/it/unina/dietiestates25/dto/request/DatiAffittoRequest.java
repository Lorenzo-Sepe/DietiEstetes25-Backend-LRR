package it.unina.dietiestates25.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatiAffittoRequest {

    private int tempoMinimo;
    private int tempoMassimo;
    private Double caparra;
}
