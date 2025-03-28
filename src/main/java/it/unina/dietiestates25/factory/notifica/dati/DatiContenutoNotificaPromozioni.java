package it.unina.dietiestates25.factory.notifica.dati;

import it.unina.dietiestates25.dto.request.NotificaPromozionaleRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatiContenutoNotificaPromozioni implements DatiContenutoNotifica {
    private String contenuto;
    private String oggetto;

    public static DatiContenutoNotifica fromRequest(NotificaPromozionaleRequest request) {
        return DatiContenutoNotificaPromozioni.builder()
                .oggetto(request.getOggetto())
                .contenuto(request.getContenuto())
                .build();
    }
}


