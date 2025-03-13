package it.unina.dietiestates25.factory.notifica.dati;

import it.unina.dietiestates25.entity.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DatiContenutoImmobile implements DatiContenutoNotifica {
    private String indirizzoImmobile;
    private double prezzo;
    private String nomeDestinatario;
    private String tipoContratto;
    private String tipoImmobile;
    private String urlImmagineImmobile;
    private String descrizione;
    private String urlAnnuncioImmobile;

    public static DatiContenutoImmobile fromAnnuncio(AnnuncioImmobiliare annuncio, User utente) {
        return DatiContenutoImmobile.builder()
                .indirizzoImmobile(costruisciIndirizzoImmobile(annuncio))
                .prezzo(ottieniPrezzoImmobile(annuncio))
                .nomeDestinatario(utente.getUsername())
                .tipoContratto(annuncio.getContratto().getTipoContratto())
                .tipoImmobile(annuncio.getImmobile().getTipologiaImmobile().toString())
                .urlImmagineImmobile(annuncio.getImmobile().getImmagini().getFirst().getUrl())
                .descrizione(annuncio.getDescrizione())
                .urlAnnuncioImmobile(annuncio.getUrl())
                .build();
    }

    private static String costruisciIndirizzoImmobile(AnnuncioImmobiliare annuncio) {
        return annuncio.getImmobile().getIndirizzo().getCitta()
                + " " + annuncio.getImmobile().getIndirizzo().getVia()
                + " " + annuncio.getImmobile().getIndirizzo().getNumeroCivico();
    }

    private static Double ottieniPrezzoImmobile(AnnuncioImmobiliare annuncio) {
        if (annuncio.getContratto() instanceof ContrattoAffitto) {
            return ((ContrattoAffitto) annuncio.getContratto()).getPrezzoAffitto();
        } else if (annuncio.getContratto() instanceof ContrattoVendita) {
            return ((ContrattoVendita) annuncio.getContratto()).getPrezzoVendita();
        } else {
            throw new IllegalArgumentException("Tipo di contratto non riconosciuto: " + annuncio.getContratto().getClass().getSimpleName());
        }
    }
}