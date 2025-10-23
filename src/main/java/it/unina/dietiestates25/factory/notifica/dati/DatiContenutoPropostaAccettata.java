package it.unina.dietiestates25.factory.notifica.dati;

import it.unina.dietiestates25.entity.ContrattoAffitto;
import it.unina.dietiestates25.entity.ContrattoVendita;
import it.unina.dietiestates25.entity.DatiImpiegato;
import it.unina.dietiestates25.entity.Proposta;
import lombok.Builder;
import lombok.Getter;

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
    private String urlProfiloAgente;
    private String nomeAgente;

    public static DatiContenutoPropostaAccettata fromProposta(Proposta proposta, DatiImpiegato datiImpiegato) {
        String urlProfiloAgente = "/agenti/" + proposta.getAnnuncio().getAgente().getId();
        String indirizzoImmobile = costruisciIndirizzoImmobile(proposta);
        Double prezzoImmobile = ottieniPrezzoImmobile(proposta);

        return DatiContenutoPropostaAccettata.builder()
                .nomeDestinatario(proposta.getNome())
                .titoloAnnuncio(proposta.getAnnuncio().getTitolo())
                .prezzoProposto(proposta.getPrezzoProposta())
                .urlImmagineImmobile(proposta.getAnnuncio().getImmobile().getImmagini().getFirst().getUrl())
                .prezzo(prezzoImmobile.toString())
                .descrizione(proposta.getAnnuncio().getDescrizione())
                .nomeAgente(datiImpiegato.getNome())
                .urlProfiloAgente(urlProfiloAgente)
                .indirizzoImmobile(indirizzoImmobile)
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
            throw new IllegalArgumentException("Tipo di contratto non riconosciuto: "
                    + proposta.getAnnuncio().getContratto().getClass().getSimpleName());
        }
    }
}