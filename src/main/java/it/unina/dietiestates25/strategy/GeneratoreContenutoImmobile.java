package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoImmobile;

public class GeneratoreContenutoImmobile implements GeneratoreContenutoNotifica<DatiContenutoImmobile> {

    @Override
    public String generaContenuto(DatiContenutoImmobile dati) {
        return "<html>"
                + "<body>"
                + "<h1>Nuova Opportunità Immobiliare</h1>"
                + "<p>Potrebbe interessarti un immobile in " + dati.getIndirizzoImmobile() + " al prezzo di " + dati.getPrezzo() + ".</p>"
                + "</body>"
                + "</html>";
    }

    @Override
    public CategoriaNotificaName getTipoNotifica() {
        return CategoriaNotificaName.OPPORTUNITA_IMMOBILE;
    }
}
