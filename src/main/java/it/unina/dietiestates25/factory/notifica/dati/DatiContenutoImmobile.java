package it.unina.dietiestates25.factory.notifica.dati;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DatiContenutoImmobile implements DatiContenutoNotifica{
    private String indirizzoImmobile;
    private double prezzo;

    private String nomeDestinatario;

    private String tipoContratto;

    private String tipoImmobile;

    private String urlImmagineImmobile;

    private String descrizione;

    private String urlAnnuncioImmobile;
}
