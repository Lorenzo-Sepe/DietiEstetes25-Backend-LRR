package it.unina.dietiestates25.factory;

import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoNotifica;
import it.unina.dietiestates25.strategy.*;

import java.util.EnumMap;
import java.util.Map;

public class GeneratoreContenutoFactory {

    private static final Map<CategoriaNotificaName, GeneratoreContenutoNotifica<? extends DatiContenutoNotifica>> generatori = new EnumMap<>(
            CategoriaNotificaName.class);

    static {
        generatori.put(CategoriaNotificaName.PROPOSTA_ACCETTATA, new GeneratoreContenutoPropostaAccettata());
        generatori.put(CategoriaNotificaName.PROPOSTA_RIFIUTATA, new GeneratoreContenutoPropostaRifiutata());
        generatori.put(CategoriaNotificaName.CONTROPROPOSTA, new GeneratoreContenutoControproposta());
        generatori.put(CategoriaNotificaName.OPPORTUNITA_IMMOBILE, new GeneratoreContenutoImmobile());
        generatori.put(CategoriaNotificaName.PROMOZIONI, new GeneratoreContenutoPromozioni());
    }

    private GeneratoreContenutoFactory() {
        // Prevent instantiation
    }

    /**
     * Restituisce il generatore di contenuto corrispondente al tipo di notifica.
     *
     * @param tipoNotifica Il tipo di notifica richiesto
     * @return Il generatore corrispondente
     */
    @SuppressWarnings("unchecked")
    public static <T extends DatiContenutoNotifica> GeneratoreContenutoNotifica<T> getGeneratore(
            CategoriaNotificaName tipoNotifica) {
        if (!generatori.containsKey(tipoNotifica)) {
            throw new IllegalArgumentException("Tipo di notifica non riconosciuto: " + tipoNotifica);
        }
        return (GeneratoreContenutoNotifica<T>) generatori.get(tipoNotifica);
    }
}
