package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.CaratteristicheAggiuntiveRequest;
import it.unina.dietiestates25.entity.CaratteristicheAggiuntive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaratteristicheAggiuntiveService {

    public CaratteristicheAggiuntive createCaratteristicheAggiuntiveFromRequest(CaratteristicheAggiuntiveRequest request){

        return CaratteristicheAggiuntive.builder()
                .ascensore(request.isAscensore())
                .balconi(request.isBalconi())
                .cantina(request.isCantina())
                .climatizzatori(request.isClimatizzatori())
                .garage(request.isGarage())
                .pannelliSolari(request.isPannelliSolari())
                .portiere(request.isPortiere())
                .giardino(request.isGiardino())
                .riscaldamentoCentralizzato(request.isRiscaldamentoCentralizzato())
                .soffitta(request.isSoffitta())
                .postiAuto(request.isPostiAuto())
                .build();
    }

    public void updateCaratteristicheAggiuntive(CaratteristicheAggiuntiveRequest request, CaratteristicheAggiuntive caratteristicheAggiuntive){
        caratteristicheAggiuntive.setAscensore(request.isAscensore());
        caratteristicheAggiuntive.setBalconi(request.isBalconi());
        caratteristicheAggiuntive.setCantina(request.isCantina());
        caratteristicheAggiuntive.setClimatizzatori(request.isClimatizzatori());
        caratteristicheAggiuntive.setGarage(request.isGarage());
        caratteristicheAggiuntive.setPannelliSolari(request.isPannelliSolari());
        caratteristicheAggiuntive.setPortiere(request.isPortiere());
        caratteristicheAggiuntive.setGiardino(request.isGiardino());
        caratteristicheAggiuntive.setRiscaldamentoCentralizzato(request.isRiscaldamentoCentralizzato());
        caratteristicheAggiuntive.setSoffitta(request.isSoffitta());
        caratteristicheAggiuntive.setPostiAuto(request.isPostiAuto());
    }
}
