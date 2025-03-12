package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoPropostaAccettata;

public class GeneratoreContenutoPropostaAccettata implements GeneratoreContenutoNotifica<DatiContenutoPropostaAccettata> {

    @Override
    public String generaContenuto(DatiContenutoPropostaAccettata dati) {
        return "<html>"
                + "<body>"
                + "<h1>Proposta Accettata</h1>"
                + "<p>Ciao " + dati.getNomeDestinatario() + ", la tua proposta #" + dati.getIdProposta() + " è stata accettata.</p>"
                + "</body>"
                + "</html>";
    }

    @Override
    public CategoriaNotificaName getTipoNotifica() {
        return CategoriaNotificaName.PROPOSTA_ACCETTATA;
    }
}
