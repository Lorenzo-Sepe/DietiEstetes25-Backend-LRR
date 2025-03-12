package it.unina.dietiestates25.strategy;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoNotifica;

public interface GeneratoreContenutoNotifica<T extends DatiContenutoNotifica> {
    /**
     * Genera il contenuto HTML per la notifica usando i dati specifici.
     *
     * @param dati DTO contenente i dati necessari
     * @return Stringa con il contenuto HTML
     */
    String generaContenuto(T dati);

    /**
     * Restituisce il tipo di notifica gestito da questo generatore.
     */
    CategoriaNotificaName getTipoNotifica();
}
