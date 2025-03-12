package it.unina.dietiestates25.factory.notifica.dati;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DatiContenutoControproposta implements DatiContenutoNotifica{
    private String nomeDestinatario;

    private String titoloAnnuncio;
    private Double prezzoProposto;
    private Double prezzoControproposta;

    private String urlImmagineImmobile;

    private String IndirizzoImmobile;

    private String prezzo;

    private String descrizione;

    private String urlProfiloAgente;

    private String nomeAgente;

}
