package it.unina.dietiestates25.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgenziaImmobiliareRequest {

    String nomeAgenzia;
    String ragioneSociale;
    String partitaIva;

    //Dati Fondatore
    String nomeFondatore;
    String cognomeFondatore;
    String emailFondatore;

}
