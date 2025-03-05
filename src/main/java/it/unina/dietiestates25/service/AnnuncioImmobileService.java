package it.unina.dietiestates25.service;


import it.unina.dietiestates25.dto.request.*;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AnnuncioImmobiliareRequest;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.ContrattoRequest;
import it.unina.dietiestates25.dto.response.AnnuncioImmobiliareResponse;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.ClasseEnergetica;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import it.unina.dietiestates25.repository.AnnuncioImmobiliareRepository;
import it.unina.dietiestates25.service.specification.AnnuncioImmobiliareSpecification;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnuncioImmobileService {

    private final AnnuncioImmobiliareRepository annuncioImmobiliareRepository;
    private final ModelMapper modelMapper;
    private final ImageUploaderService imageUploaderService;
    @Transactional
    public String creaAnnuncioImmobiliare(AnnuncioImmobiliareRequest request){

        User agenteImmobliare = UserContex.getUserCurrent();

        Immobile immobile = getImmobileByRequest(request.getImmobile());

        Contratto contratto = getContrattoFromRequest(request.getContratto());

        AnnuncioImmobiliare annuncioImmobiliare = AnnuncioImmobiliare.builder()
                .immobile(immobile)
                .contratto(contratto)
                .agente(agenteImmobliare)
                .dataPubblicazione(LocalDateTime.now())
                .descrizione(request.getDescrizione())
                .titolo(request.getTitolo())
                .proposte(new ArrayList<>())
                .build();

        AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.save(annuncioImmobiliare);
        int immobileId = annuncio.getImmobile().getId();
        List <MultipartFile> files = recuperaMultipartFiles(request.getImmobile().getImmagini());
        List<String> imageUrls = imageUploaderService.salvaImmaginiAnnuncio(files, immobileId);
        List<ImmaginiImmobile> immaginiImmobili = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            annuncio.getImmobile().getImmagini().get(i).setUrl(imageUrls.get(i));
        }
        annuncioImmobiliareRepository.save(annuncio);

        return "Annuncio creato con successo";
    }

    // Metodo per recuperare tutti i MultipartFile da una lista di ImmaginiImmobiliRequest
    public List<MultipartFile> recuperaMultipartFiles(List<ImmaginiImmobiliRequest> immaginiRequests) {
        return immaginiRequests.stream()
                .map(ImmaginiImmobiliRequest::getFile)
                .collect(Collectors.toList());
    }
    private Immobile getImmobileByRequest(ImmobileRequest request){

        Immobile immobile = Immobile.builder()
                .tipologiaImmobile(getEnumTipologiaImmobileFromString(request.getTipologiaImmobile()))
                .metriQuadri(request.getMetriQuadri())
                .classeEnergetica(getEnumClasseEnergeticaFromString(request.getClasseEnergetica()))
                .numeroServizi(request.getNumeroServizi())
                .numeroStanze(request.getNumeroStanze())
                .numeroDiPiani(request.getNumeroDiPiani())
                .indirizzo(getIndirizzoFromRequest(request.getIndirizzo()))
                .caratteristicheAggiuntive(getCaratteristicheAggiuntiveFromRequest(request.getCaratteristicheAggiuntive()))
                .build();

        immobile.setImmagini(getListaImmaginiFromRequest(request.getImmagini(),immobile));

        return immobile;
    }

    private TipologiaImmobile getEnumTipologiaImmobileFromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Valore nullo o vuoto");
        }
        try {
            return TipologiaImmobile.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nessun valore dell'enumerazione TipologiaImmobile corrispondente a: " + value);
        }
    }

    private ClasseEnergetica getEnumClasseEnergeticaFromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Valore nullo o vuoto");
        }
        try {
            return ClasseEnergetica.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nessun valore dell'enumerazione ClasseEnergetica corrispondente a: " + value);
        }
    }

    private Indirizzo getIndirizzoFromRequest(IndirizzoRequest request){

        Indirizzo indirizzo = Indirizzo.builder()
                .nazione(request.getNazione())
                .cap(request.getCap())
                .citta(request.getCitta())
                .provincia(request.getProvincia())
                .via(request.getVia())
                .numeroCivico(request.getNumeroCivico())
                .longitudine(request.getLongitudine())
                .latitudine(request.getLatitudine())
                .build();

        return indirizzo;
    }

    private CaratteristicheAggiuntive getCaratteristicheAggiuntiveFromRequest(CaratteristicheAggiuntiveRequest request){

        CaratteristicheAggiuntive caratteristicheAggiuntive = CaratteristicheAggiuntive.builder()
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

        return caratteristicheAggiuntive;
    }

    private Contratto getContrattoFromRequest(ContrattoRequest request){

        Contratto contratto;

        if(request.getDatiAffittoRequest() != null)
            contratto = getContrattoAffittoFromRequest(request);

        else
            contratto = getContrattoVenditaFromRequest(request);

        return contratto;
    }

    private ContrattoAffitto getContrattoAffittoFromRequest(ContrattoRequest request){

        DatiAffittoRequest datiAffittoRequest = request.getDatiAffittoRequest();

        ContrattoAffitto contrattoAffitto = ContrattoAffitto.builder()
                .caparra(datiAffittoRequest.getCaparra())
                .tempoMinimo(datiAffittoRequest.getTempoMinimo())
                .tempoMassimo(datiAffittoRequest.getTempoMassimo())
                .prezzo(request.getPrezzo())
                .build();

        return contrattoAffitto;
    }

    private ContrattoVendita getContrattoVenditaFromRequest(ContrattoRequest request){

        DatiVenditaRequest datiVenditaRequest = request.getDatiVenditaRequest();

        ContrattoVendita contrattoVendita = ContrattoVendita.builder()
                .mutuoEstinto(datiVenditaRequest.isMutuoEstinto())
                .prezzo(request.getPrezzo())
                .build();

        return contrattoVendita;
    }

    private List<ImmaginiImmobile> getListaImmaginiFromRequest(List<ImmaginiImmobiliRequest> immagini, Immobile immobile){

        List<ImmaginiImmobile> immaginiImmobili = new ArrayList<>();

        immagini.stream().forEach(img -> {

            ImmaginiImmobile immagine = ImmaginiImmobile.builder()
                    .url("placeholder.it")
                    .descrizione(img.getDescrizione())
                    .immobile(immobile)
                    .build();
            immaginiImmobili.add(immagine);
        });

        return immaginiImmobili;
    }


    public List<AnnuncioImmobiliare> cercaAnnunci(FiltroAnnuncio filtro) {
        Specification<AnnuncioImmobiliare> spec = Specification
                .where(AnnuncioImmobiliareSpecification.conTitolo(filtro.getTitolo()))
                .and(AnnuncioImmobiliareSpecification.conTipologiaImmobile(filtro.getTipologiaImmobile()))
                .and(AnnuncioImmobiliareSpecification.conRangePrezzo(filtro.getPrezzoMin(), filtro.getPrezzoMax()))
                .and(AnnuncioImmobiliareSpecification.conRangeMetriQuadri(filtro.getMetriQuadriMin(), filtro.getMetriQuadriMax()))
               // .and(AnnuncioImmobiliareSpecification.conLocalizzazione(filtro.getLatCentro(), filtro.getLonCentro(), filtro.getRaggioKm()))
                .and(AnnuncioImmobiliareSpecification.conCaratteristicheAggiuntive(filtro.getBalconi(), filtro.getGarage(), filtro.getPannelliSolari()));

        return annuncioImmobiliareRepository.findAll(spec);
    }

    public List<AnnuncioImmobiliareResponse> cercaAnnunci() {
        List<AnnuncioImmobiliare> annunci= annuncioImmobiliareRepository.findAll();
        List<AnnuncioImmobiliareResponse> annunciResponse = new ArrayList<>();

        //mapping from entity to response
        for(AnnuncioImmobiliare annuncio: annunci){
            AnnuncioImmobiliareResponse response = modelMapper.map(annuncio, AnnuncioImmobiliareResponse.class);
            annunciResponse.add(response);
        }
        return annunciResponse;
    }
}
