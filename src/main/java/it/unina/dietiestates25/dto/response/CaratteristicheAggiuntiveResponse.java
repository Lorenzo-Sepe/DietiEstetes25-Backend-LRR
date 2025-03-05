package it.unina.dietiestates25.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaratteristicheAggiuntiveResponse {
    private int id;
    private boolean balconi;
    private boolean garage;
    private boolean postiAuto;
    private boolean giardino;
    private boolean ascensore;
    private boolean portiere;
    private boolean riscaldamentoCentralizzato;
    private boolean climatizzatori;
    private boolean pannelliSolari;
    private boolean cantina;
    private boolean soffitta;
}