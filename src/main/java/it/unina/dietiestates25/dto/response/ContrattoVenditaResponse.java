package it.unina.dietiestates25.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContrattoVenditaResponse {

    private Double prezzoVendita;
    private boolean mutuoEstinto;
}
