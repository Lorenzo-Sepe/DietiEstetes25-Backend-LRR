package it.unina.dietiestates25.factory.notifica.dati;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DatiContenutoPropostaRifiutata implements DatiContenutoNotifica{
    private String nomeDestinatario;
    private String motivazione;
}
