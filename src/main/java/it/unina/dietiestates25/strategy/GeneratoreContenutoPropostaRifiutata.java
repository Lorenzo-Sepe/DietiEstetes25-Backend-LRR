package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoPropostaRifiutata;

public class GeneratoreContenutoPropostaRifiutata implements GeneratoreContenutoNotifica<DatiContenutoPropostaRifiutata> {

    @Override
    public String generaContenuto(DatiContenutoPropostaRifiutata dati) {
        return "<html>"
                + "<body>"
                + "<h1>Proposta Rifiutata</h1>"
                + "<p>Ciao " + dati.getNomeDestinatario() + ", la tua proposta è stata rifiutata per il seguente motivo: " + dati.getMotivazione() + ".</p>"
                + "</body>"
                + "</html>";
    }

    @Override
    public CategoriaNotificaName getTipoNotifica() {
        return CategoriaNotificaName.PROPOSTA_RIFIUTATA;
    }
}




