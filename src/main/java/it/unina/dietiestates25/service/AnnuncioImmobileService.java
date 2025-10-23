package it.unina.dietiestates25.service;


import it.unina.dietiestates25.dto.request.*;
import it.unina.dietiestates25.dto.request.agenziaImmobiliare.AnnuncioImmobiliareRequest;
import it.unina.dietiestates25.dto.response.*;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.*;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import it.unina.dietiestates25.repository.AnnuncioImmobiliareRepository;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.UserRepository;
import it.unina.dietiestates25.service.specification.AnnuncioImmobiliareSpecification;
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


@Service
@RequiredArgsConstructor
@Slf4j
public class AnnuncioImmobileService {

    private final ContrattoService contrattoService;
    private final ImmobileService immobileService;
    private final AgenziaImmobiliareService agenziaImmobiliareService;
    private final AnnuncioImmobiliareRepository annuncioImmobiliareRepository;
    private final ImageUploaderService imageUploaderService;
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;
    private final NotificaService notificaService;
    private final UserRepository userRepository;
    private final DatiImpiegatoRepository datiImpiegatoRepository;

    //-------------------------------------------------------CREA ANNUNCIO-------------------------------------------------------

    @Transactional
    public String creaAnnuncioImmobiliare(AnnuncioImmobiliareRequest request, List<MultipartFile> immaginiList){

        User agenteImmobiliare = UserContex.getUserCurrent();
        Immobile immobile = immobileService.createImmobileByRequest(request.getImmobile(), immaginiList);
        Contratto contratto = contrattoService.createContrattoFromRequest(request.getContratto());
        AnnuncioImmobiliare annuncioImmobiliare = AnnuncioImmobiliare.builder()
                .immobile(immobile)
                .contratto(contratto)
                .agente(agenteImmobiliare)
                .dataPubblicazione(LocalDateTime.now())
                .descrizione(request.getDescrizione())
                .titolo(request.getTitolo())
                .proposte(new ArrayList<>())
                .build();

        imageUploaderService.updateImmaginiAnnuncio(request.getImmobile().getImmagini(),annuncioImmobiliare);
        annuncioImmobiliareRepository.save(annuncioImmobiliare);

        try{
            notificaService.inviaNotificaPerNuovoAnnuncioImmobiliare(annuncioImmobiliare);

        }catch (Exception e){
            //do Nothing
            log.error("Errore nell'invio della notifica per il nuovo annuncio immobiliare");
        }

        return "Annuncio creato con successo";
    }



    //-------------------------------------------------------GET ANNUNCI-------------------------------------------------------

    public AnnuncioImmobiliareResponse getAnnuncioImmobiliare(int id) {
        AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annuncio immobiliare", "id", id));

        User agente = userRepository.findById(annuncio.getAgente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", annuncio.getAgente().getId()));

        DatiImpiegato datiImpiegato = datiImpiegatoRepository.findByUser_Id(agente.getId())
                .orElseThrow(() -> new ResourceNotFoundException("DatiImpiegato", "id", agente.getId()));

        ImmobileResponse immobileResponse = ImmobileResponse.fromEntityToDto(annuncio.getImmobile());

        AgenziaImmobiliare agenzia = agenziaImmobiliareService.getAgenziaImmobiliare(agente.getEmail());

        AnnuncioImmobiliareResponse annuncioImmobiliareResponse = AnnuncioImmobiliareResponse.builder()
                .immobile(immobileResponse)
                .titolo(annuncio.getTitolo())
                .agente(DipendenteResponse.fromEntityToDto(datiImpiegato))
                .contratto(ContrattoResponse.fromEntityToDto(annuncio.getContratto()))
                .descrizione(annuncio.getDescrizione())
                .dataPubblicazione(annuncio.getDataPubblicazione().toString())
                .proposte(PropostaResponse.fromListEntityToDto(annuncio.getProposte()))
                .dataCreazione(annuncio.getDataPubblicazione())
                .build();

        annuncioImmobiliareResponse.getAgente().setAgenzia(agenzia.getNomeAzienda());

        return annuncioImmobiliareResponse;
    }

    public List<AnnuncioImmobiliareResponse> cercaAnnunci(FiltroAnnuncioDTO filtro) {

        AuthorityName ruoloUserCurrent = UserContex.getRoleCurrent();
        List<AnnuncioImmobiliare> annunci = getAnnunciByRuolo(ruoloUserCurrent,filtro);
        List<AnnuncioImmobiliareResponse> annunciResponse= new ArrayList<>();

        for(AnnuncioImmobiliare annuncio : annunci){
            ImmobileResponse immobileResponse = ImmobileResponse.fromEntityToDto(annuncio.getImmobile());
            List<PropostaResponse> proposteResponse = PropostaResponse.fromListEntityToDto(annuncio.getProposte());
            ContrattoResponse contrattoResponse = ContrattoResponse.fromEntityToDto(annuncio.getContratto());
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

    private List<AnnuncioImmobiliare> getAnnunciByRuolo(AuthorityName ruolo, FiltroAnnuncioDTO filtro){

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

            User agente = userRepository.findByEmail(filtro.getAgenteCreatoreAnnuncio()).orElseThrow(
                    () -> new ResourceNotFoundException("User", "email", filtro.getAgenteCreatoreAnnuncio()));


            annunci = annuncioImmobiliareRepository.findByAgente(agente);
        }

        return annunci;
    }

    private Specification<AnnuncioImmobiliare> getSpecificationQuery(FiltroAnnuncioDTO filtro){

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


    //------------------------------------------------------GET NUMERO DI ANNUNCI------------------------------------------------------

    public long getNumeroAnnunci(FiltroAnnuncioDTO filtro){

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
    public String modificaAnnuncioImmobiliare(int id, AnnuncioImmobiliareRequest request, List<MultipartFile> immaginiList) {

        AnnuncioImmobiliare annuncio = annuncioImmobiliareRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Annuncio immobiliare", "id", id));

        verificaPermessoModificaAnnuncio(annuncio);

        immobileService.updateImmobile(request.getImmobile(),annuncio.getImmobile(),immaginiList);
        contrattoService.updateContratto(request.getContratto(),annuncio);
        annuncio.setTitolo(request.getTitolo());
        annuncio.setDescrizione(request.getDescrizione());

        imageUploaderService.updateImmaginiAnnuncio(request.getImmobile().getImmagini(),annuncio);

        annuncioImmobiliareRepository.save(annuncio);

        return "Annuncio modificato con successo";

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
