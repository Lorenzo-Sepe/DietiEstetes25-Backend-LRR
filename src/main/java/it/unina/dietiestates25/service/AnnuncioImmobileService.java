package it.unina.dietiestates25.service;


import it.unina.dietiestates25.dto.request.*;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AnnuncioImmobiliareRequest;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.ContrattoRequest;
import it.unina.dietiestates25.dto.response.*;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.AuthorityName;
import it.unina.dietiestates25.entity.enumeration.ClasseEnergetica;
import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import it.unina.dietiestates25.repository.AnnuncioImmobiliareRepository;
import it.unina.dietiestates25.service.specification.AnnuncioImmobiliareSpecification;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
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
    private final ImageUploaderService imageUploaderService;
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;

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

        AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.save(annuncioImmobiliare);

        immobile.setImmagini(getListaImmaginiFromRequest(request.getImmobile().getImmagini(),immobile));
        updateImmaginiAnnuncio(request.getImmobile().getImmagini(),annuncio);

        annuncioImmobiliareRepository.save(annuncio);

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

    private void updateImmaginiAnnuncio(List<ImmaginiImmobiliRequest> immaginiRequest, AnnuncioImmobiliare annuncio){

        int immobileId = annuncio.getImmobile().getId();

        List <MultipartFile> immaginiFile = getListMultipartFilesFromRequest(immaginiRequest);

        List<String> urlImmagini = imageUploaderService.salvaImmaginiAnnuncioToBlob(immaginiFile, immobileId);

        setUrlToImmaginiAnnuncio(annuncio.getImmobile().getImmagini(),immaginiFile,urlImmagini);
    }

    private List<MultipartFile> getListMultipartFilesFromRequest(List<ImmaginiImmobiliRequest> immaginiRequests) {
        return immaginiRequests.stream()
                .map(ImmaginiImmobiliRequest::getFile)
                .collect(Collectors.toList());
    }

    private void setUrlToImmaginiAnnuncio(List<ImmaginiImmobile> immaginiImmobile,List<MultipartFile> immaginiFile,List<String> urlImmagini){

        for (int i = 0; i < immaginiFile.size(); i++) {
            immaginiImmobile.get(i).setUrl(urlImmagini.get(i));
        }
    }

    //-------------------------------------------------------GET ANNUNCI-------------------------------------------------------

    public List<AnnuncioImmobiliareResponse> cercaAnnunci(FiltroAnnuncio filtro) {

        Specification<AnnuncioImmobiliare> spec = getSpecificationQuery(filtro);

        List<AnnuncioImmobiliare> annunci = annuncioImmobiliareRepository.findAll(spec);

        List<AnnuncioImmobiliareResponse> annunciResponse= new ArrayList<>();

        for(AnnuncioImmobiliare annuncio : annunci){

            ImmobileResponse immobileResponse = getImmobileResponse(annuncio.getImmobile());
            List<PropostaResponse> proposteResponse = getListPropostaResponse(annuncio.getProposte());
            ContrattoResponse contrattoResponse = getContrattoResponse(annuncio.getContratto());
            UserResponse agenteCreatoreAnnuncio = getAgenteCreatoreAnnuncio(annuncio.getAgente());

            AnnuncioImmobiliareResponse annuncioResponse = AnnuncioImmobiliareResponse.builder()
                    .titolo(annuncio.getTitolo())
                    .descrizione(annuncio.getDescrizione())
                    .immobile(immobileResponse)
                    .proposte(proposteResponse)
                    .agente(agenteCreatoreAnnuncio)
                    .contratto(contrattoResponse)
                    .build();

            annunciResponse.add(annuncioResponse);
        }

        return annunciResponse;
    }

    //TODO cambiare la logica del range del prezzo in seguito al cambiamento fatto in contratto (prezzo è passato ai figli)
    private Specification<AnnuncioImmobiliare> getSpecificationQuery(FiltroAnnuncio filtro){

        Specification<AnnuncioImmobiliare> specfication = Specification
                .where(AnnuncioImmobiliareSpecification.conTitolo(filtro.getTitolo()))
                .and(AnnuncioImmobiliareSpecification.conTipologiaImmobile(filtro.getTipologiaImmobile()))
                //.and(AnnuncioImmobiliareSpecification.conRangePrezzo(filtro.getPrezzoMin(), filtro.getPrezzoMax()))
                .and(AnnuncioImmobiliareSpecification.conRangeMetriQuadri(filtro.getMetriQuadriMin(), filtro.getMetriQuadriMax()))
                //.and(AnnuncioImmobiliareSpecification.conLocalizzazione(filtro.getLatCentro(), filtro.getLonCentro(), filtro.getRaggioKm()))
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
    }

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

    //TODO: implementare metodo per modificare annuncio immobiliare
    public String modificaAnnuncioImmobiliare(int id, AnnuncioImmobiliareRequest request) {


        AnnuncioImmobiliare annuncioImmobiliare = annuncioImmobiliareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Annuncio immobiliare", "id", id));

        verificaPermessoModificaAnnuncio(annuncioImmobiliare);

        Immobile immobile = annuncioImmobiliare.getImmobile();

        Contratto contratto = getContrattoFromRequest(request.getContratto());

        User agenteImmobliare = UserContex.getUserCurrent();

        annuncioImmobiliare.setImmobile(immobile);
        annuncioImmobiliare.setContratto(contratto);
        annuncioImmobiliare.setAgente(agenteImmobliare);
        annuncioImmobiliare.setDescrizione(request.getDescrizione());
        annuncioImmobiliare.setTitolo(request.getTitolo());

        AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.save(annuncioImmobiliare);

        updateImmaginiAnnuncio(request.getImmobile().getImmagini(),annuncio);

        annuncioImmobiliareRepository.save(annuncio);

        return "Annuncio modificato con successo";


    }

    //-------------------------------------------------------CANCELLA ANNUNCIO-------------------------------------------------------


    public void cancellaAnnuncioImmobiliare(int id) {
        verificaPermessoModificaAnnuncio(annuncioImmobiliareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Annuncio immobiliare", "id", id)));
        annuncioImmobiliareRepository.deleteById(id);
    }

    //-------------------------------------------------------METODI DI SUPPORTO PER MODIFICA/CANCELLA ANNUNCIO-------------------------------------------------------


    // Verifica se l'utente corrente ha il permesso di modificare l'annuncio
    public void verificaPermessoModificaAnnuncio(AnnuncioImmobiliare annuncio) {
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
