package it.unina.dietiestates25.dto.request;

import lombok.Getter;

@Getter
public class AgenziaImmobiliareRequest {

    String nomeAgenzia;
    String ragioneSociale;
    String partitaIva;

    //Dati Fondatore
    String nomeFondatore;
    String cognomeFondatore;
    String emailFondatore;

}
