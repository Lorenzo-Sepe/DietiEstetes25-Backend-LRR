package it.unina.dietiestates25.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaratteristicheAggiuntiveRequest {

    private boolean balconi = false;
    private boolean garage = false;
    private boolean postiAuto = false;
    private boolean giardino = false;
    private boolean ascensore = false;
    private boolean portiere = false;
    private boolean riscaldamentoCentralizzato = false;
    private boolean climatizzatori = false;
    private boolean pannelliSolari = false;
    private boolean cantina = false;
    private boolean soffitta = false;
}
