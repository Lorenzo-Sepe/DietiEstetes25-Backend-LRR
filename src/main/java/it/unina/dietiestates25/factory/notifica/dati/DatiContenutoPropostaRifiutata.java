package it.unina.dietiestates25.factory.notifica.dati;

import it.unina.dietiestates25.entity.ContrattoAffitto;
import it.unina.dietiestates25.entity.ContrattoVendita;
import it.unina.dietiestates25.entity.Proposta;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DatiContenutoPropostaRifiutata implements DatiContenutoNotifica {
    private String nomeDestinatario;
    private String titoloAnnuncio;
    private Double prezzoProposto;
    private String urlImmagineImmobile;
    private String indirizzoImmobile; // Corretto il nome della variabile
    private String prezzo;
    private String descrizione;
    private String urlAnnuncioImmobile;

    public static DatiContenutoPropostaRifiutata fromProposta(Proposta proposta) {
        return DatiContenutoPropostaRifiutata.builder()
                .nomeDestinatario(proposta.getNome())
                .titoloAnnuncio(proposta.getAnnuncio().getTitolo())
                .prezzoProposto(proposta.getPrezzoProposta())
                .urlImmagineImmobile(proposta.getAnnuncio().getImmobile().getImmagini().getFirst().getUrl())
                .prezzo(ottieniPrezzoImmobile(proposta).toString()) // Assumendo che il prezzo sia lo stesso della
                                                                    // proposta
                .descrizione(proposta.getAnnuncio().getDescrizione())
                .urlAnnuncioImmobile(proposta.getAnnuncio().getUrl()) // Assumendo che ci sia un metodo per ottenere
                                                                      // l'URL dell'annuncio
                .indirizzoImmobile(costruisciIndirizzoImmobile(proposta))
                .build();
    }

    private static String costruisciIndirizzoImmobile(Proposta proposta) {
        return proposta.getAnnuncio().getImmobile().getIndirizzo().getCitta()
                + " " + proposta.getAnnuncio().getImmobile().getIndirizzo().getVia()
                + " " + proposta.getAnnuncio().getImmobile().getIndirizzo().getNumeroCivico();
    }

    private static Double ottieniPrezzoImmobile(Proposta proposta) {
        if (proposta.getAnnuncio().getContratto() instanceof ContrattoAffitto contrattoAffitto) {
            return contrattoAffitto.getPrezzoAffitto();
        } else if (proposta.getAnnuncio().getContratto() instanceof ContrattoVendita contrattovendita) {
            return contrattovendita.getPrezzoVendita();
        } else {
            throw new IllegalArgumentException("Tipo di contratto non riconosciuto: "
                    + proposta.getAnnuncio().getContratto().getClass().getSimpleName());
        }
    }
}