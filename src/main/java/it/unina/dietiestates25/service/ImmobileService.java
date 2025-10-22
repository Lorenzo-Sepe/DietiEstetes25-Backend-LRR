package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.ImmaginiImmobiliRequest;
import it.unina.dietiestates25.dto.request.ImmobileRequest;
import it.unina.dietiestates25.entity.Immobile;
import it.unina.dietiestates25.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImmobileService {
    private final IndirizzoService indirizzoService;
    private final TipologiaImmobileService tipologiaImmobileService;
    private final ClasseEnergeticaService classeEnergeticaService;
    private final CaratteristicheAggiuntiveService caratteristicheAggiuntiveService;

    public Immobile createImmobileByRequest(ImmobileRequest request, List<MultipartFile> immaginiList){

        if (request == null){
            throw new BadRequestException("Richiesta non puo` essere vuota");
        }

        if (immaginiList != null  && request.getImmagini() != null) {
            List<ImmaginiImmobiliRequest> immagini = request.getImmagini();
            for (int i = 0; i < immagini.size(); i++) {
                if (i < immaginiList.size()) {
                    immagini.get(i).setFile(immaginiList.get(i));
                }
            }
        }

        return Immobile.builder()
                .tipologiaImmobile(tipologiaImmobileService.checkEnumTipologiaImmobileFromString(request.getTipologiaImmobile()))
                .metriQuadri(request.getMetriQuadri())
                .classeEnergetica(classeEnergeticaService.checkEnumClasseEnergeticaFromString(request.getClasseEnergetica()))
                .numeroServizi(request.getNumeroServizi())
                .numeroStanze(request.getNumeroStanze())
                .numeroDiPiani(request.getNumeroDiPiani())
                .indirizzo(indirizzoService.createIndirizzoFromRequest(request.getIndirizzo()))
                .caratteristicheAggiuntive(caratteristicheAggiuntiveService.createCaratteristicheAggiuntiveFromRequest(request.getCaratteristicheAggiuntive()))
                .build();
    }

    public void updateImmobile(ImmobileRequest request,Immobile immobile, List<MultipartFile> immaginiList){

        if (request == null){
            throw new BadRequestException("Richiesta non puo` essere vuota");
        }

        if (immaginiList != null  && request.getImmagini() != null) {
            List<ImmaginiImmobiliRequest> immagini = request.getImmagini();
            for (int i = 0; i < immagini.size(); i++) {
                if (i < immaginiList.size()) {
                    immagini.get(i).setFile(immaginiList.get(i));
                }
            }
        }

        immobile.setTipologiaImmobile(tipologiaImmobileService.checkEnumTipologiaImmobileFromString(request.getTipologiaImmobile()));
        immobile.setMetriQuadri(request.getMetriQuadri());
        immobile.setClasseEnergetica(classeEnergeticaService.checkEnumClasseEnergeticaFromString(request.getClasseEnergetica()));
        immobile.setNumeroServizi(request.getNumeroServizi());
        immobile.setNumeroStanze(request.getNumeroStanze());
        immobile.setNumeroDiPiani(request.getNumeroDiPiani());
        indirizzoService.updateIndirizzo(request.getIndirizzo(),immobile.getIndirizzo());
        caratteristicheAggiuntiveService.updateCaratteristicheAggiuntive(request.getCaratteristicheAggiuntive(),immobile.getCaratteristicheAggiuntive());
    }


}
