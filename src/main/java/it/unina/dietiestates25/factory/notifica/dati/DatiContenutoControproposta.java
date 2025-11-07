package it.unina.dietiestates25.factory.notifica.dati;

import it.unina.dietiestates25.entity.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DatiContenutoControproposta implements DatiContenutoNotifica {
    private String nomeDestinatario;
    private String titoloAnnuncio;
    private Double prezzoProposto;
    private Double prezzoControproposta;
    private String urlImmagineImmobile;
    private String indirizzoImmobile;
    private String prezzo;
    private String descrizione;
    private java.util.List<Contatto> contattiAgente;
    private String nomeAgente;
    private String urlAnnuncioImmobile;


    public static DatiContenutoControproposta fromProposta(Proposta proposta, DatiImpiegato datiImpiegato) {
        Double prezzoImmobile = ottieniPrezzoImmobile(proposta);
        String urlProfiloAgente = "/agenti/" + proposta.getAnnuncio().getAgente().getId();
        String indirizzoImmobile = costruisciIndirizzoImmobile(proposta);

        return DatiContenutoControproposta.builder()
                .nomeDestinatario(proposta.getNome())
                .titoloAnnuncio(proposta.getAnnuncio().getTitolo())
                .prezzoProposto(proposta.getPrezzoProposta())
                .prezzoControproposta(proposta.getControproposta())
                .urlImmagineImmobile(proposta.getAnnuncio().getImmobile().getImmagini().getFirst().getUrl())
                .prezzo(prezzoImmobile.toString())
                .descrizione(proposta.getAnnuncio().getDescrizione())
                .nomeAgente(datiImpiegato.getNome())
                .contattiAgente(datiImpiegato.getContatti() != null ?
                        new java.util.ArrayList<>(datiImpiegato.getContatti()) :
                        java.util.Collections.emptyList())
                .indirizzoImmobile(indirizzoImmobile)
                .urlAnnuncioImmobile(proposta.getAnnuncio().getUrl())
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
        } else if (proposta.getAnnuncio().getContratto() instanceof ContrattoVendita contrattoVendita) {
            return contrattoVendita.getPrezzoVendita();
        } else {
            throw new IllegalArgumentException("Tipo di contratto non riconosciuto: " + proposta.getAnnuncio().getContratto().getClass().getSimpleName());
        }
    }
}