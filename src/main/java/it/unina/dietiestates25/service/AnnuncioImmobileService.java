package it.unina.dietiestates25.service;


import it.unina.dietiestates25.dto.request.*;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AnnuncioImmobiliareRequest;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.ContrattoRequest;
import it.unina.dietiestates25.dto.response.*;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.*;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import it.unina.dietiestates25.repository.AnnuncioImmobiliareRepository;
import it.unina.dietiestates25.service.specification.AnnuncioImmobiliareSpecification;
import it.unina.dietiestates25.utils.NearbyPlacesChecker;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnuncioImmobileService {

    private final AnnuncioImmobiliareRepository annuncioImmobiliareRepository;
    private final ImageUploaderService imageUploaderService;
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;
    private final NearbyPlacesChecker nearbyPlacesChecker ;
    private final NotificaService notificaService;

    //-------------------------------------------------------CREA ANNUNCIO-------------------------------------------------------

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

        annuncioImmobiliareRepository.save(annuncioImmobiliare);

        updateImmaginiAnnuncio(request.getImmobile().getImmagini(),annuncioImmobiliare);
        annuncioImmobiliareRepository.save(annuncioImmobiliare);

        try{

            notificaService.inviaNotificaPerNuovoAnnuncioImmobiliare(annuncioImmobiliare);

        }catch (Exception e){
            //do Nothing
        }

        return "Annuncio creato con successo";
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

        //immobile.setImmagini(getListaImmaginiFromRequest(request.getImmagini(),immobile));

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
                .luoghiVicini(getPostiVicino(request.getLatitudine(),request.getLongitudine()))
                .build();

        return indirizzo;
    }

    private Set<VicinoA> getPostiVicino(double latitudine, double longitudine){

        Set<VicinoA> postiVicini = nearbyPlacesChecker.getPuntiInteresseVicini(latitudine,longitudine);

        return postiVicini;
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

        if(request.getTipoDiContratto().equals("AFFITTO")){
            contratto = getContrattoAffittoFromRequest(request);
        }

        else{
            contratto = getContrattoVenditaFromRequest(request);
        }

        return contratto;
    }

    private ContrattoAffitto getContrattoAffittoFromRequest(ContrattoRequest request){

        DatiAffittoRequest datiAffittoRequest = request.getDatiAffittoRequest();

        ContrattoAffitto contrattoAffitto = ContrattoAffitto.builder()
                .build();

        contrattoAffitto.setCaparra(datiAffittoRequest.getCaparra());
        contrattoAffitto.setTempoMinimo(datiAffittoRequest.getTempoMinimo());
        contrattoAffitto.setTempoMassimo(datiAffittoRequest.getTempoMassimo());
        contrattoAffitto.setPrezzoAffitto(datiAffittoRequest.getPrezzo());
        contrattoAffitto.setTipoContratto("AFFITTO");

        return contrattoAffitto;
    }

    private ContrattoVendita getContrattoVenditaFromRequest(ContrattoRequest request){

        DatiVenditaRequest datiVenditaRequest = request.getDatiVenditaRequest();

        ContrattoVendita contrattoVendita = ContrattoVendita.builder()
                .build();

        contrattoVendita.setMutuoEstinto(datiVenditaRequest.isMutuoEstinto());
        contrattoVendita.setPrezzoVendita(datiVenditaRequest.getPrezzo());
        contrattoVendita.setTipoContratto(TipoContratto.VENDITA.toString());


        return contrattoVendita;
    }

    private void updateImmaginiAnnuncio(List<ImmaginiImmobiliRequest> immaginiRequest, AnnuncioImmobiliare annuncio){

        int immobileId = annuncio.getImmobile().getId();

        List <MultipartFile> immaginiFile = getListMultipartFilesFromRequest(immaginiRequest);

        List<String> urlImmagini = imageUploaderService.salvaImmaginiAnnuncioToBlob(immaginiFile, immobileId,0);

        annuncio.getImmobile().setImmagini(getListaImmaginiImmobili(urlImmagini,immaginiRequest,annuncio.getImmobile()));
    }

    private List<MultipartFile> getListMultipartFilesFromRequest(List<ImmaginiImmobiliRequest> immaginiRequests) {
        return immaginiRequests.stream()
                .map(ImmaginiImmobiliRequest::getFile)
                .collect(Collectors.toList());
    }

    private List<ImmaginiImmobile> getListaImmaginiImmobili(List<String> urlImmagini,List<ImmaginiImmobiliRequest> request,Immobile immobile){

        List<ImmaginiImmobile> immaginiImmobile = new ArrayList<>();

        for(int i=0; i< request.size();i++){

            ImmaginiImmobile immagine = ImmaginiImmobile.builder()
                    .immobile(immobile)
                    .descrizione(request.get(i).getDescrizione())
                    .url(urlImmagini.get(i))
                    .build();
            immaginiImmobile.add(immagine);
        }

        return immaginiImmobile;
    }

    //-------------------------------------------------------GET ANNUNCI-------------------------------------------------------

    public List<AnnuncioImmobiliareResponse> cercaAnnunci(FiltroAnnuncio filtro) {

        AuthorityName ruoloUserCurrent = UserContex.getRoleCurrent();

        List<AnnuncioImmobiliare> annunci = getAnnunciByRuolo(ruoloUserCurrent,filtro);

        List<AnnuncioImmobiliareResponse> annunciResponse= new ArrayList<>();

        for(AnnuncioImmobiliare annuncio : annunci){

            ImmobileResponse immobileResponse = getImmobileResponse(annuncio.getImmobile());
            //List<PropostaResponse> proposteResponse = getListPropostaResponse(annuncio.getProposte());
            ContrattoResponse contrattoResponse = getContrattoResponse(annuncio.getContratto());
            UserResponse agenteCreatoreAnnuncio = getAgenteCreatoreAnnuncio(annuncio.getAgente());

            AnnuncioImmobiliareResponse annuncioResponse = AnnuncioImmobiliareResponse.builder()
                    .id(annuncio.getId())
                    .titolo(annuncio.getTitolo())
                    .descrizione(annuncio.getDescrizione())
                    .immobile(immobileResponse)
                    //.proposte(proposteResponse)
                    .agente(agenteCreatoreAnnuncio)
                    .contratto(contrattoResponse)
                    .build();

            annunciResponse.add(annuncioResponse);
        }

        return annunciResponse;
    }

    private List<AnnuncioImmobiliare> getAnnunciByRuolo(AuthorityName ruolo,FiltroAnnuncio filtro){

        List<AnnuncioImmobiliare> annunci;

        Pageable pageable = Pageable.ofSize(filtro.getNumeroDiElementiPerPagina()).withPage(filtro.getNumeroPagina()-1);

        if(ruolo == null || ruolo == AuthorityName.MEMBER ){

            Specification<AnnuncioImmobiliare> spec = getSpecificationQuery(filtro);

            annunci = annuncioImmobiliareRepository.findAll(spec,pageable).getContent();

        } else if(ruolo == AuthorityName.AGENT){

            annunci = annuncioImmobiliareRepository.findByAgente(UserContex.getUserCurrent(),pageable);

        } else {

            AgenziaImmobiliare agenziaImmobiliare = agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(UserContex.getUserCurrent()).get();

            Set<User> dipendentiAgenziaImmobiliare = agenziaImmobiliare.getDipendenti();

            annunci = annuncioImmobiliareRepository.findByAgenteIn(dipendentiAgenziaImmobiliare,pageable);
        }

        return annunci;
    }

    private Specification<AnnuncioImmobiliare> getSpecificationQuery(FiltroAnnuncio filtro){
        if( filtro.getProvincia()!=null && !filtro.getProvincia().isBlank()){
            filtro.setLatCentro(null);
            filtro.setLonCentro(null);
            filtro.setRaggioKm(null);
        }
        Specification<AnnuncioImmobiliare> specfication = Specification
                .where(AnnuncioImmobiliareSpecification.conTitolo(filtro.getTitolo()))
                .and(AnnuncioImmobiliareSpecification.conTipologiaImmobile(filtro.getTipologiaImmobile()))
                .and(AnnuncioImmobiliareSpecification.conRangePrezzo(filtro.getPrezzoMin(), filtro.getPrezzoMax()))
                .and(AnnuncioImmobiliareSpecification.conRangeMetriQuadri(filtro.getMetriQuadriMin(), filtro.getMetriQuadriMax()))
                .and(AnnuncioImmobiliareSpecification.conLocalizzazione(filtro.getLatCentro(), filtro.getLonCentro(), filtro.getRaggioKm()))
                .and(AnnuncioImmobiliareSpecification.conProvincia(filtro.getProvincia()))
                .and(AnnuncioImmobiliareSpecification.conCaratteristicheAggiuntive(filtro.getBalconi(), filtro.getGarage(), filtro.getPannelliSolari()));

        return specfication;
    }

    private ImmobileResponse getImmobileResponse(Immobile immobile){

        IndirizzoResponse indirizzoResponse = getIndirizzoResponse(immobile.getIndirizzo());
        CaratteristicheAggiuntiveResponse caratteristicheAggiuntiveResponse = getCaratteristicheAggiuntiveResponse(immobile.getCaratteristicheAggiuntive());
        List<ImmaginiImmobileResponse> immaginiImmobileResponse = getListImmaginiImmobiliResponse(immobile.getImmagini());

        ImmobileResponse immobileResponse = ImmobileResponse.builder()
                .tipologiaImmobile(immobile.getTipologiaImmobile().toString())
                .metriQuadri(immobile.getMetriQuadri())
                .classeEnergetica(immobile.getClasseEnergetica().toString())
                .numeroServizi(immobile.getNumeroServizi())
                .numeroStanze(immobile.getNumeroStanze())
                .numeroDiPiani(immobile.getNumeroDiPiani())
                .indirizzo(indirizzoResponse)
                .caratteristicheAggiuntive(caratteristicheAggiuntiveResponse)
                .immagini(immaginiImmobileResponse)
                .build();

        return immobileResponse;
    }

    private IndirizzoResponse getIndirizzoResponse(Indirizzo indirizzoImmobile){

        IndirizzoResponse indirizzoResponse = IndirizzoResponse.builder()
                .via(indirizzoImmobile.getVia())
                .numeroCivico(indirizzoImmobile.getNumeroCivico())
                .citta(indirizzoImmobile.getCitta())
                .cap(indirizzoImmobile.getCap())
                .provincia(indirizzoImmobile.getCap())
                .nazione(indirizzoImmobile.getNazione())
                .longitudine(indirizzoImmobile.getLongitudine())
                .latitudine(indirizzoImmobile.getLatitudine())
                .vicinoA(indirizzoImmobile.getLuoghiVicini())
                .build();

        return indirizzoResponse;
    }

    private CaratteristicheAggiuntiveResponse getCaratteristicheAggiuntiveResponse(CaratteristicheAggiuntive caratteristicheAggiuntive){

        CaratteristicheAggiuntiveResponse caratteristicheAggiuntiveResponse = CaratteristicheAggiuntiveResponse.builder()
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
                .build();

        return caratteristicheAggiuntiveResponse;
    }

    private List<ImmaginiImmobileResponse> getListImmaginiImmobiliResponse(List<ImmaginiImmobile> immaginiImmobile){

        List<ImmaginiImmobileResponse> immaginiImmobileResponse = new ArrayList<>();

        for(ImmaginiImmobile immagine : immaginiImmobile){

            ImmaginiImmobileResponse immagineResponse = ImmaginiImmobileResponse.builder()
                    .url(immagine.getUrl())
                    .descrizione(immagine.getDescrizione())
                    .build();

            immaginiImmobileResponse.add(immagineResponse);
        }

        return immaginiImmobileResponse;
    }

    //TODO questa parte di codice andrebbe eliminata perché c'è una get proposte da chiamare a parte
    /*
    private List<PropostaResponse> getListPropostaResponse(List<Proposta> proposte){

        List<PropostaResponse> proposteRespose = new ArrayList<>();

        for(Proposta proposta : proposte){

            UserResponse userResponse = getUserProponente(proposta);
            ContattoResponse contattoResponse = getContattoResponse(proposta);

            PropostaResponse propostaResponse = PropostaResponse.builder()
                    .prezzoProposta(proposta.getPrezzoProposta())
                    .controproposta(proposta.getPrezzoProposta())
                    .stato(proposta.getStato().toString())
                    .utente(userResponse)
                    .contatto(contattoResponse)
                    .build();

            proposteRespose.add(propostaResponse);
        }

        return proposteRespose;
    }

    private UserResponse getUserProponente(Proposta proposta){

        UserResponse userProponente = UserResponse.builder()
                .email(proposta.getUser().getEmail())
                .username(proposta.getUser().getUsername())
                .urlFotoProfilo(proposta.getUser().getUrlFotoProfilo())
                .build();

        return userProponente;
    }

    private ContattoResponse getContattoResponse(Proposta proposta){

        ContattoResponse contattoResponse = ContattoResponse.builder()
                .tipo(proposta.getContatto().getTipo())
                .valore(proposta.getContatto().getValore())
                .build();

        return contattoResponse;
    }*/

    private ContrattoResponse getContrattoResponse(Contratto contratto){

        ContrattoResponse contrattoResponse = ContrattoResponse.builder().build();

        if(contratto instanceof ContrattoAffitto){

            ContrattoAffittoResponse contrattoAffittoResponse = getContrattoAffitto((ContrattoAffitto)contratto);

            contrattoResponse.setContrattoAffittoResponse(contrattoAffittoResponse);
            contrattoResponse.setTipoContratto("AFFITTO");

        }else if(contratto instanceof ContrattoVendita){

            ContrattoVenditaResponse contrattoVenditaResponse = getContrattoVendita((ContrattoVendita) contratto);

            contrattoResponse.setTipoContratto("VENDITA");
            contrattoResponse.setContrattoVenditaResponse(contrattoVenditaResponse);
        }

        return contrattoResponse;
    }

    private ContrattoAffittoResponse getContrattoAffitto(ContrattoAffitto contratto){

        ContrattoAffittoResponse contrattoAffittoResponse = ContrattoAffittoResponse.builder()
                .caparra(contratto.getCaparra())
                .prezzoAffitto(contratto.getPrezzoAffitto())
                .tempoMinimo( contratto.getTempoMinimo())
                .tempoMassimo(contratto.getTempoMassimo())
                .build();

        return contrattoAffittoResponse;
    }

    private ContrattoVenditaResponse getContrattoVendita(ContrattoVendita contratto){

        ContrattoVenditaResponse contrattoVenditaResponse = ContrattoVenditaResponse.builder()
                .mutuoEstinto(contratto.isMutuoEstinto())
                .prezzoVendita(contratto.getPrezzoVendita())
                .build();

        return contrattoVenditaResponse;
    }

    private UserResponse getAgenteCreatoreAnnuncio(User agente){

        UserResponse agenteCreatoreAnnuncio = UserResponse.builder()
                .email(agente.getEmail())
                .username(agente.getUsername())
                .urlFotoProfilo(agente.getUrlFotoProfilo())
                .build();

        return agenteCreatoreAnnuncio;
    }

    //-------------------------------------------------------MODIFICA ANNUNCIO-------------------------------------------------------

    public String modificaAnnuncioImmobiliare(int id, AnnuncioImmobiliareRequest request) {

        AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Annuncio immobiliare", "id", id));

        verificaPermessoModificaAnnuncio(annuncio);

        updateImmobile(request.getImmobile(),annuncio.getImmobile());
        updateContratto(request.getContratto(),annuncio);
        annuncio.setTitolo(request.getTitolo());
        annuncio.setDescrizione(request.getDescrizione());

        //TODO testare la funzione e valutare se si può scrivere un codice migliore
        updateImmagini(request.getImmobile().getImmagini(),annuncio);

        annuncioImmobiliareRepository.save(annuncio);

        return "Annuncio modificato con successo";

    }

    private void updateImmobile(ImmobileRequest request,Immobile immobile){

        immobile.setTipologiaImmobile(getEnumTipologiaImmobileFromString(request.getTipologiaImmobile()));
        immobile.setMetriQuadri(request.getMetriQuadri());
        immobile.setClasseEnergetica(getEnumClasseEnergeticaFromString(request.getClasseEnergetica()));
        immobile.setNumeroServizi(request.getNumeroServizi());
        immobile.setNumeroStanze(request.getNumeroStanze());
        immobile.setNumeroDiPiani(request.getNumeroDiPiani());
        updateIndirizzo(request.getIndirizzo(),immobile.getIndirizzo());
        updateCaratteristicheAggiuntive(request.getCaratteristicheAggiuntive(),immobile.getCaratteristicheAggiuntive());
    }

    private void updateIndirizzo(IndirizzoRequest request,Indirizzo indirizzo){

        indirizzo.setNazione(request.getNazione());
        indirizzo.setCap(request.getCap());
        indirizzo.setCitta(request.getCitta());
        indirizzo.setProvincia(request.getProvincia());
        indirizzo.setVia(request.getVia());
        indirizzo.setNumeroCivico(request.getNumeroCivico());
        indirizzo.setLongitudine(request.getLongitudine());
        indirizzo.setLatitudine(request.getLatitudine());
    }

    private void updateCaratteristicheAggiuntive(CaratteristicheAggiuntiveRequest request, CaratteristicheAggiuntive caratteristicheAggiuntive){

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

    private void updateContratto(ContrattoRequest request,AnnuncioImmobiliare annuncio){

        if(request.getTipoDiContratto().equals("AFFITTO")){

            annuncio.setContratto(new ContrattoAffitto());

            updateContrattoAffitto(request.getDatiAffittoRequest(),(ContrattoAffitto)annuncio.getContratto());
        }

        else{

            annuncio.setContratto(new ContrattoVendita());

            updateContrattoVendita(request.getDatiVenditaRequest(), (ContrattoVendita)annuncio.getContratto());
        }
    }

    private void updateContrattoAffitto(DatiAffittoRequest request,ContrattoAffitto contratto){

        contratto.setCaparra(request.getCaparra());
        contratto.setTempoMinimo(request.getTempoMinimo());
        contratto.setTempoMassimo(request.getTempoMassimo());
        contratto.setPrezzoAffitto(request.getPrezzo());
        contratto.setTipoContratto("AFFITTO");
    }

    private void updateContrattoVendita(DatiVenditaRequest request,ContrattoVendita contratto){

        contratto.setMutuoEstinto(request.isMutuoEstinto());
        contratto.setPrezzoVendita(request.getPrezzo());
        contratto.setTipoContratto(TipoContratto.VENDITA.toString());
    }

    private void updateImmagini(List<ImmaginiImmobiliRequest> request, AnnuncioImmobiliare annuncio){

        annuncio.getImmobile().getImmagini().clear();

        int numeroImmaginiGiaInserite = addImmaginiGiaEsistentiToNuovaListaImmagini(request,annuncio);

        List<String> urls = imageUploaderService.salvaImmaginiAnnuncioToBlob(getListaImmaginiFile(request), annuncio.getId(),numeroImmaginiGiaInserite+1);

        addNuoveImmaginiToNuovaListaImmagini(urls,request,annuncio);

    }

    private int addImmaginiGiaEsistentiToNuovaListaImmagini(List<ImmaginiImmobiliRequest> request, AnnuncioImmobiliare annuncio){

        int countInserimenti = 0;

        for(ImmaginiImmobiliRequest immagine : request ){

                if(immagine.getUrlImmagineEsistente() != null){
                ImmaginiImmobile immagineGiaEsistente = ImmaginiImmobile.builder()
                        .immobile(annuncio.getImmobile())
                        .descrizione(immagine.getDescrizione())
                        .url(immagine.getUrlImmagineEsistente())
                        .build();

                annuncio.getImmobile().getImmagini().add(immagineGiaEsistente);

                countInserimenti++;
            }
        }

        return countInserimenti;
    }

    private List<MultipartFile> getListaImmaginiFile(List<ImmaginiImmobiliRequest> request){

        List<MultipartFile> immaginiFile = new ArrayList<>();

        for(ImmaginiImmobiliRequest immagine : request ){

            if(immagine.getFile() != null){

                immaginiFile.add(immagine.getFile());
            }
        }

        return immaginiFile;
    }

    private void addNuoveImmaginiToNuovaListaImmagini(List<String> urls, List<ImmaginiImmobiliRequest> request,AnnuncioImmobiliare annuncio) {

        int countFile = 0;

        for(int i=0;i<request.size();i++){

            if(request.get(i).getFile() != null){

                ImmaginiImmobile immaginiImmobile = ImmaginiImmobile.builder()
                        .immobile(annuncio.getImmobile())
                        .descrizione(request.get(i).getDescrizione())
                        .url(urls.get(countFile))
                        .build();

                annuncio.getImmobile().getImmagini().add(immaginiImmobile);

                countFile++;
            }
        }
    }

    //-------------------------------------------------------CANCELLA ANNUNCIO-------------------------------------------------------


    public String cancellaAnnuncioImmobiliare(int id) {

        verificaPermessoModificaAnnuncio(annuncioImmobiliareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Annuncio immobiliare", "id", id)));

        annuncioImmobiliareRepository.deleteById(id);

        return "Annuncio cancellato";
    }

    //-------------------------------------------------------METODI DI SUPPORTO PER MODIFICA/CANCELLA ANNUNCIO-------------------------------------------------------


    // Verifica se l'utente corrente ha il permesso di modificare l'annuncio
    private void verificaPermessoModificaAnnuncio(AnnuncioImmobiliare annuncio) {
        if(UserContex.getRoleCurrent() == AuthorityName.MEMBER) {
            throw new AccessDeniedException("Non hai il permesso di modificare questo annuncio");
        }else if(UserContex.getRoleCurrent() == AuthorityName.ADMIN && !isAdminDellaAgenzia(annuncio)) {
            throw new AccessDeniedException("Questo annuncio è di un altra Agenzia Immobiliare\nNon hai il permesso di modificare questo annuncio");

        }
        if (UserContex.getRoleCurrent() == AuthorityName.AGENT && !isProprietarioAnnuncio(annuncio)) {
            throw new AccessDeniedException("Non hai il permesso di modificare questo annuncio\n puoi modificar solo i tuoi Annunci immobiliari");
        }
    }

    // Verifica se l'utente corrente è il proprietario dell'annuncio
    private boolean isProprietarioAnnuncio(AnnuncioImmobiliare annuncio) {
        User utenteCorrente = UserContex.getUserCurrent();
        return annuncio.getAgente().equals(utenteCorrente);
    }

    // Verifica se l'utente corrente è il capo dell'agenzia
    private boolean isAdminDellaAgenzia(AnnuncioImmobiliare annuncio) {
        User utenteCorrente = UserContex.getUserCurrent();
        AgenziaImmobiliare agenziaDelUtenteCorrente= agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(utenteCorrente)
                .orElseThrow(() -> new AccessDeniedException("Non sei un dipendente di nessuna agenzia immobiliare"));
        User agenteAnnuncio = annuncio.getAgente();
        AgenziaImmobiliare agenziaAssociataAnnuncio = agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(agenteAnnuncio).orElseThrow(() -> new ResourceNotFoundException("Agenzia Immobiliare associata all'annuncio","id",annuncio.getId()));
        return agenziaAssociataAnnuncio.equals(agenziaDelUtenteCorrente);
    }
}
