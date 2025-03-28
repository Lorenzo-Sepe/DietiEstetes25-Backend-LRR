package it.unina.dietiestates25.dto.response;

import it.unina.dietiestates25.entity.CaratteristicheAggiuntive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaratteristicheAggiuntiveResponse {
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
    private String descrizioneAggiuntiva;

    public static CaratteristicheAggiuntiveResponse fromEntityToDto(CaratteristicheAggiuntive caratteristicheAggiuntive) {
        return CaratteristicheAggiuntiveResponse.builder()
                .balconi(caratteristicheAggiuntive.isBalconi())
                .garage(caratteristicheAggiuntive.isGarage())
                .postiAuto(caratteristicheAggiuntive.isPostiAuto())
                .giardino(caratteristicheAggiuntive.isGiardino())
                .ascensore(caratteristicheAggiuntive.isAscensore())
                .portiere(caratteristicheAggiuntive.isPortiere())
                .riscaldamentoCentralizzato(caratteristicheAggiuntive.isRiscaldamentoCentralizzato())
                .climatizzatori(caratteristicheAggiuntive.isClimatizzatori())
                .pannelliSolari(caratteristicheAggiuntive.isPannelliSolari())
                .cantina(caratteristicheAggiuntive.isCantina())
                .soffitta(caratteristicheAggiuntive.isSoffitta())
                .descrizioneAggiuntiva(caratteristicheAggiuntive.getDescrizioneAggiuntiva())
                .build();
    }
}