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
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.specification.AnnuncioImmobiliareSpecification;
import it.unina.dietiestates25.utils.NearbyPlacesChecker;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AnnuncioImmobileService {

    private final AnnuncioImmobiliareRepository annuncioImmobiliareRepository;
    private final ImageUploaderService imageUploaderService;
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;
    private final NearbyPlacesChecker nearbyPlacesChecker ;
    private final NotificaService notificaService;
    private final UserRepository userRepository;
    private final DatiImpiegatoRepository datiImpiegatoRepository;

    //-------------------------------------------------------CREA ANNUNCIO-------------------------------------------------------

    @Transactional
    public String creaAnnuncioImmobiliare(AnnuncioImmobiliareRequest request){

        User agenteImmobliare = UserContex.getUserCurrent();
        Immobile immobile = createImmobileByRequest(request.getImmobile());
        Contratto contratto = createContrattoFromRequest(request.getContratto());
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
            log.error("Errore nell'invio della notifica per il nuovo annuncio immobiliare");
        }

        return "Annuncio creato con successo";
    }

    private Immobile createImmobileByRequest(ImmobileRequest request){

        Immobile immobile = Immobile.builder()
                .tipologiaImmobile(getEnumTipologiaImmobileFromString(request.getTipologiaImmobile()))
                .metriQuadri(request.getMetriQuadri())
                .classeEnergetica(getEnumClasseEnergeticaFromString(request.getClasseEnergetica()))
                .numeroServizi(request.getNumeroServizi())
                .numeroStanze(request.getNumeroStanze())
                .numeroDiPiani(request.getNumeroDiPiani())
                .indirizzo(createIndirizzoFromRequest(request.getIndirizzo()))
                .caratteristicheAggiuntive(createCaratteristicheAggiuntiveFromRequest(request.getCaratteristicheAggiuntive()))
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

    private Indirizzo createIndirizzoFromRequest(IndirizzoRequest request){
        return Indirizzo.builder()
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
    }

    private Set<VicinoA> getPostiVicino(double latitudine, double longitudine){

        return nearbyPlacesChecker.getPuntiInteresseVicini(latitudine,longitudine);
    }

    private CaratteristicheAggiuntive createCaratteristicheAggiuntiveFromRequest(CaratteristicheAggiuntiveRequest request){

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
                .descrizioneAggiuntiva(request.getDescrizioneAggiuntiva())
                .build();

        return caratteristicheAggiuntive;
    }

    private Contratto createContrattoFromRequest(ContrattoRequest request){

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

    public AnnuncioImmobiliareResponse getAnnuncioImmobiliare(int id) {
        AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annuncio immobiliare", "id", id));

        User agente = userRepository.findById(annuncio.getAgente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", annuncio.getAgente().getId()));

        DatiImpiegato datiImpiegato = datiImpiegatoRepository.findByUser_Id(agente.getId())
                .orElseThrow(() -> new ResourceNotFoundException("DatiImpiegato", "id", agente.getId()));

        ImmobileResponse immobileResponse = getImmobileResponse(annuncio.getImmobile());

        return AnnuncioImmobiliareResponse.builder()
                .immobile(immobileResponse)
                .titolo(annuncio.getTitolo())
                .agente(DipendenteResponse.fromEntityToDto(datiImpiegato))
                .contratto(ContrattoResponse.fromEntityToDto(annuncio.getContratto()))
                .descrizione(annuncio.getDescrizione())
                .dataPubblicazione(annuncio.getDataPubblicazione().toString())
                .proposte(PropostaResponse.fromListEntityToDto(annuncio.getProposte()))
                .dataCreazione(annuncio.getDataPubblicazione())
                .build();
    }

    public List<AnnuncioImmobiliareResponse> cercaAnnunci(FiltroAnnuncio filtro) {

        AuthorityName ruoloUserCurrent = UserContex.getRoleCurrent();
        List<AnnuncioImmobiliare> annunci = getAnnunciByRuolo(ruoloUserCurrent,filtro);
        List<AnnuncioImmobiliareResponse> annunciResponse= new ArrayList<>();

        for(AnnuncioImmobiliare annuncio : annunci){
            ImmobileResponse immobileResponse = getImmobileResponse(annuncio.getImmobile());
            List<PropostaResponse> proposteResponse = getListPropostaResponse(annuncio.getProposte());
            ContrattoResponse contrattoResponse = getContrattoResponse(annuncio.getContratto());
            DatiImpiegato datiImpiegato = datiImpiegatoRepository.findDatiImpiegatoByUser(annuncio.getAgente())
                    .orElseThrow(() -> new ResourceNotFoundException("Dati impiegato", "user", annuncio.getAgente().getId()));

            AnnuncioImmobiliareResponse annuncioResponse = AnnuncioImmobiliareResponse.builder()
                    .id(annuncio.getId())
                    .dataCreazione(annuncio.getDataPubblicazione())
                    .titolo(annuncio.getTitolo())
                    .descrizione(annuncio.getDescrizione())
                    .dataPubblicazione(annuncio.getDataPubblicazione().toString())
                    .immobile(immobileResponse)
                    .proposte(proposteResponse)
                    .agente(DipendenteResponse.fromEntityToDto(datiImpiegato))
                    .contratto(contrattoResponse)
                    .build();

            annunciResponse.add(annuncioResponse);
        }

        return annunciResponse;
    }

    private List<AnnuncioImmobiliare> getAnnunciByRuolo(AuthorityName ruolo, FiltroAnnuncio filtro){

        List<AnnuncioImmobiliare> annunci;
        Pageable pageable = null;

        if(filtro.getNumeroPagina() != null && filtro.getNumeroDiElementiPerPagina() != null){
            pageable = Pageable.ofSize(filtro.getNumeroDiElementiPerPagina()).withPage(filtro.getNumeroPagina()-1);
        }

        if(ruolo == null || ruolo == AuthorityName.MEMBER ){

            Specification<AnnuncioImmobiliare> spec = getSpecificationQuery(filtro);

            if(pageable != null){
                annunci = annuncioImmobiliareRepository.findAll(spec,pageable).getContent();
            }else {
                annunci = annuncioImmobiliareRepository.findAll(spec);
            }

        } else if(ruolo == AuthorityName.AGENT){

            annunci = annuncioImmobiliareRepository.findByAgente(UserContex.getUserCurrent(),pageable);

        } else {

            /*AgenziaImmobiliare agenziaImmobiliare = agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(UserContex.getUserCurrent())
                    .orElseThrow(() -> new AccessDeniedException("Utente non appartenente a nessuna agenzia immobiliare"));*/

            //Set<User> dipendentiAgenziaImmobiliare = agenziaImmobiliare.getDipendenti();

            User agente = userRepository.findByEmail(filtro.getAgenteCreatoreAnnuncio()).get();

            annunci = annuncioImmobiliareRepository.findByAgente(agente);
        }

        return annunci;
    }

    private Specification<AnnuncioImmobiliare> getSpecificationQuery(FiltroAnnuncio filtro){

        return Specification
                .where(AnnuncioImmobiliareSpecification.conTitolo(filtro.getTitolo()))
                .and(AnnuncioImmobiliareSpecification.conTipologiaImmobile(filtro.getTipologiaImmobile()))
                .and(AnnuncioImmobiliareSpecification.conTipoContratto(filtro.getTipologiaContratto().toString()))
                .and(AnnuncioImmobiliareSpecification.conRangePrezzo(filtro.getPrezzoMin(), filtro.getPrezzoMax()))
                .and(AnnuncioImmobiliareSpecification.conRangeMetriQuadri(filtro.getMetriQuadriMin(), filtro.getMetriQuadriMax()))
                .and(AnnuncioImmobiliareSpecification.conLocalizzazione(filtro.getLatCentro(), filtro.getLonCentro(), filtro.getRaggioKm()))
                .and(AnnuncioImmobiliareSpecification.conProvincia(filtro.getProvincia()))
                .and(AnnuncioImmobiliareSpecification.conCaratteristicheAggiuntive(filtro))
                .and(AnnuncioImmobiliareSpecification.ordinaPerPrezzoAsc(filtro.isOrdinePrezzoAsc(),filtro.getTipologiaContratto().toString()))
                .and(AnnuncioImmobiliareSpecification.ordinaPerPrezzoDesc(filtro.isOrdinePrezzoDesc(),filtro.getTipologiaContratto().toString()))
                .and(AnnuncioImmobiliareSpecification.ordinaPerDataDesc(filtro.isOrdineDataDesc()))
                .and(AnnuncioImmobiliareSpecification.ordinaPerDataAsc(filtro.isOrdineDataAsc()));
    }

    private ImmobileResponse getImmobileResponse(Immobile immobile){

        IndirizzoResponse indirizzoResponse = getIndirizzoResponse(immobile.getIndirizzo());
        CaratteristicheAggiuntiveResponse caratteristicheAggiuntiveResponse = getCaratteristicheAggiuntiveResponse(immobile.getCaratteristicheAggiuntive());
        List<ImmaginiImmobileResponse> immaginiImmobileResponse = getListImmaginiImmobiliResponse(immobile.getImmagini());

        return ImmobileResponse.builder()
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
    }

    private IndirizzoResponse getIndirizzoResponse(Indirizzo indirizzoImmobile){

        return IndirizzoResponse.builder()
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
    }

    private CaratteristicheAggiuntiveResponse getCaratteristicheAggiuntiveResponse(CaratteristicheAggiuntive caratteristicheAggiuntive){

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

    //TODO durante la creazione della tabella ho scoperto che mi serveno insieme agli annunci quindi lo lascerei
    private List<PropostaResponse> getListPropostaResponse(List<Proposta> proposte){

        List<PropostaResponse> proposteRespose = new ArrayList<>();

        for(Proposta proposta : proposte){

            DatiUserPropostaResponse datiProponente = getDatiProponente(proposta);

            PropostaResponse propostaResponse = PropostaResponse.builder()

                    .idProposta(proposta.getId())
                    .prezzoProposta(proposta.getPrezzoProposta())
                    .controproposta(proposta.getControproposta())
                    .stato(proposta.getStato().toString())
                    .datiProponente(datiProponente)
                    .build();

            proposteRespose.add(propostaResponse);
        }

        return proposteRespose;
    }

    private DatiUserPropostaResponse getDatiProponente(Proposta proposta){

        ContattoResponse contattoResponse = getContattoResponse(proposta);

        return DatiUserPropostaResponse.builder()
                .nome(proposta.getNome())
                .cognome(proposta.getCognome())
                .contatto(contattoResponse)
                .build();
    }

    private ContattoResponse getContattoResponse(Proposta proposta){

        return ContattoResponse.builder()
                .tipo(proposta.getContatto().getTipo())
                .valore(proposta.getContatto().getValore())
                .build();
    }

    private ContrattoResponse getContrattoResponse(Contratto contratto){

        ContrattoResponse contrattoResponse = ContrattoResponse.builder().build();

        if(contratto instanceof ContrattoAffitto){

            ContrattoAffittoResponse contrattoAffittoResponse = getContrattoAffitto((ContrattoAffitto)contratto);
            contrattoResponse.setContrattoAffittoResponse(contrattoAffittoResponse);
            contrattoResponse.setTipoContratto("AFFITTO");

        }else if(contratto instanceof ContrattoVendita contrattoVendita){

            ContrattoVenditaResponse contrattoVenditaResponse = getContrattoVendita((ContrattoVendita) contratto);

            contrattoResponse.setTipoContratto("VENDITA");
            contrattoResponse.setContrattoVenditaResponse(contrattoVenditaResponse);
        }

        return contrattoResponse;
    }

    private ContrattoAffittoResponse getContrattoAffitto(ContrattoAffitto contratto){

        return ContrattoAffittoResponse.builder()
                .caparra(contratto.getCaparra())
                .prezzoAffitto(contratto.getPrezzoAffitto())
                .tempoMinimo( contratto.getTempoMinimo())
                .tempoMassimo(contratto.getTempoMassimo())
                .build();
    }

    private ContrattoVenditaResponse getContrattoVendita(ContrattoVendita contratto){

        return ContrattoVenditaResponse.builder()
                .mutuoEstinto(contratto.isMutuoEstinto())
                .prezzoVendita(contratto.getPrezzoVendita())
                .build();
    }

    private AgenteAnnuncioImmobiliareResponse getAgenteCreatoreAnnuncio(User agente){

        return AgenteAnnuncioImmobiliareResponse.builder()
                .email(agente.getEmail())
                .urlFotoProfilo(agente.getUrlFotoProfilo())
                .build();
    }

    //------------------------------------------------------GET NUMERO DI ANNUNCI------------------------------------------------------

    public long getNumeroAnnunci(FiltroAnnuncio filtro){

        long numeroDiAnnunci;

        AuthorityName ruoloUserCurrent = UserContex.getRoleCurrent();

        if(ruoloUserCurrent == null || ruoloUserCurrent == AuthorityName.MEMBER){

            Specification<AnnuncioImmobiliare> spec = getSpecificationQuery(filtro);

            numeroDiAnnunci = annuncioImmobiliareRepository.count(spec);

        }else if(ruoloUserCurrent == AuthorityName.AGENT){

            numeroDiAnnunci = annuncioImmobiliareRepository.countByAgente(UserContex.getUserCurrent());

        }else{

            AgenziaImmobiliare agenziaImmobiliare = agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(UserContex.getUserCurrent())
                    .orElseThrow(() -> new AccessDeniedException("Utente non appartenente a nessuna agenzia immobiliare"));

            Set<User> dipendentiAgenziaImmobiliare = agenziaImmobiliare.getDipendenti();

            numeroDiAnnunci = annuncioImmobiliareRepository.countByAgenteIn(dipendentiAgenziaImmobiliare);
        }

        return numeroDiAnnunci;
    }

    //-------------------------------------------------------MODIFICA ANNUNCIO-------------------------------------------------------

    @Transactional
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
        verificaPermessoModificaAnnuncio(annuncioImmobiliareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annuncio immobiliare", "id", id)));

        annuncioImmobiliareRepository.deleteById(id);
        return "Annuncio cancellato";
    }

    //-------------------------------------------------------METODI DI SUPPORTO PER MODIFICA/CANCELLA ANNUNCIO-------------------------------------------------------


    // Verifica se l'utente corrente ha il permesso di modificare l'annuncio
    private void verificaPermessoModificaAnnuncio(AnnuncioImmobiliare annuncio) {
        if(UserContex.getRoleCurrent() == AuthorityName.MEMBER) {
            throw new AccessDeniedException("Non hai il permesso di modificare questo annuncio");
        }else if(UserContex.getRoleCurrent() == AuthorityName.MANAGER && !isManagerDellaAgenzia(annuncio)) {
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
    private boolean isManagerDellaAgenzia(AnnuncioImmobiliare annuncio) {
        User utenteCorrente = UserContex.getUserCurrent();
        AgenziaImmobiliare agenziaDelUtenteCorrente= agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(utenteCorrente)
                .orElseThrow(() -> new AccessDeniedException("Non sei un dipendente di nessuna agenzia immobiliare"));
        User agenteAnnuncio = annuncio.getAgente();
        AgenziaImmobiliare agenziaAssociataAnnuncio = agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(agenteAnnuncio).orElseThrow(() -> new ResourceNotFoundException("Agenzia Immobiliare associata all'annuncio","id",annuncio.getId()));
        return agenziaAssociataAnnuncio.equals(agenziaDelUtenteCorrente);
    }



}
