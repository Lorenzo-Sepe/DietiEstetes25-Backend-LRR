package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoControproposta;

public class GeneratoreContenutoControproposta implements GeneratoreContenutoNotifica<DatiContenutoControproposta> {

    @Override
    public String generaContenuto(DatiContenutoControproposta dati) {
        return "<html>"
                + "<body>"
                + "<h1>Controproposta Ricevuta</h1>"
                + "<p>Ciao " + dati.getNomeDestinatario() + ", hai ricevuto una controproposta per la proposta #" + dati.getIdControproposta() + ".</p>"
                + "</body>"
                + "</html>";
    }

    @Override
    public CategoriaNotificaName getTipoNotifica() {
        return CategoriaNotificaName.CONTROPROPOSTA;
    }
}
