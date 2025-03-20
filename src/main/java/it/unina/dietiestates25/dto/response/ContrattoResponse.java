package it.unina.dietiestates25.dto.response;


import it.unina.dietiestates25.entity.Contratto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContrattoResponse {

    String tipoContratto;
    ContrattoVenditaResponse contrattoVenditaResponse;
    ContrattoAffittoResponse contrattoAffittoResponse;


}
