package it.unina.dietiestates25.factory.notifica.dati;

import it.unina.dietiestates25.entity.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;

@Getter
@Builder
public class DatiContenutoPropostaAccettata implements DatiContenutoNotifica {
    private String nomeDestinatario;
    private String titoloAnnuncio;
    private Double prezzoProposto;
    private String urlImmagineImmobile;
    private String indirizzoImmobile;
    private String prezzo;
    private String descrizione;
    private String nomeAgente;
    private java.util.List<Contatto> contattiAgente;
    private String urlAnnuncioImmobile;


    public static DatiContenutoPropostaAccettata fromProposta(Proposta proposta, DatiImpiegato datiImpiegato) {
        String indirizzoImmobile = costruisciIndirizzoImmobile(proposta);
        Double prezzoImmobile = ottieniPrezzoImmobile(proposta);
        DatiContenutoPropostaAccettata dati = DatiContenutoPropostaAccettata.builder()
                .nomeDestinatario(proposta.getNome())
                .titoloAnnuncio(proposta.getAnnuncio().getTitolo())
                .prezzoProposto(proposta.getPrezzoProposta())
                .urlImmagineImmobile(proposta.getAnnuncio().getImmobile().getImmagini().getFirst().getUrl())
                .prezzo(prezzoImmobile.toString())
                .descrizione(proposta.getAnnuncio().getDescrizione())
                .nomeAgente(datiImpiegato.getNome())
                .contattiAgente(datiImpiegato.getContatti() != null ?
                        new ArrayList<>(datiImpiegato.getContatti()) :
                        Collections.emptyList())
                .indirizzoImmobile(indirizzoImmobile)
                .urlAnnuncioImmobile(proposta.getAnnuncio().getUrl())
                .build();
        return dati;
    }

    private static String costruisciIndirizzoImmobile(Proposta proposta) {
        return proposta.getAnnuncio().getImmobile().getIndirizzo().getCitta() + ", " +
                proposta.getAnnuncio().getImmobile().getIndirizzo().getVia() + " " +
                proposta.getAnnuncio().getImmobile().getIndirizzo().getNumeroCivico();
    }

    private static Double ottieniPrezzoImmobile(Proposta proposta) {
        Object contratto = proposta.getAnnuncio().getContratto();
        return switch (contratto) {
            case ContrattoAffitto contrattoAffitto -> contrattoAffitto.getPrezzoAffitto();
            case ContrattoVendita contrattoVendita -> contrattoVendita.getPrezzoVendita();
            default -> throw new IllegalArgumentException(
                    "Tipo di contratto non riconosciuto: " + contratto.getClass().getSimpleName());
        };
    }
}
