package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoNotificaPromozioni;

public class GeneratoreContenutoPromozioni implements GeneratoreContenutoNotifica<DatiContenutoNotificaPromozioni> {

    @Override
    public String generaContenuto(DatiContenutoNotificaPromozioni dati) {
        return dati.getContenuto();
    }

    @Override
    public CategoriaNotificaName getTipoNotifica() {
        return CategoriaNotificaName.PROMOZIONI;
    }
}
