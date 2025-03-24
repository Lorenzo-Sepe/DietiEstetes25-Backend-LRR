package it.unina.dietiestates25.entity.enumeration;

import lombok.ToString;

@ToString
public enum CategoriaNotificaName {
    PROPOSTA_ACCETTATA,
    PROPOSTA_RIFIUTATA,
    CONTROPROPOSTA,
    OPPORTUNITA_IMMOBILE,
    PROMOZIONI;

    @Override
    public String toString() {
        return name();  // Restituisce solo il nome dell'enum (ad esempio "PROPOSTA_ACCETTATA")
    }
}
