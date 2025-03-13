package it.unina.dietiestates25.factory.notifica.dati;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatiContenutoNotificaPromozioni implements DatiContenutoNotifica {
    private String contenuto;
}


