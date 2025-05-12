package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.CriteriDiRicercaUtenti;
import it.unina.dietiestates25.dto.request.FiltroNotificaRequest;
import it.unina.dietiestates25.dto.request.NotificaPromozionaleRequest;
import it.unina.dietiestates25.dto.response.NotificaResponse;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.exception.UnauthorizedException;
import it.unina.dietiestates25.factory.GeneratoreContenutoFactory;
import it.unina.dietiestates25.factory.notifica.dati.*;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import it.unina.dietiestates25.repository.CategoriaNotificaRepository;
import it.unina.dietiestates25.repository.DatiImpiegatoRepository;
import it.unina.dietiestates25.repository.NotificaRepository;
import it.unina.dietiestates25.strategy.GeneratoreContenutoNotifica;
import it.unina.dietiestates25.utils.UserContex;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificaService {

    private final NotificaRepository notificaRepository;
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;
    private final DatiImpiegatoRepository datiImpiegatoRepository;
    private final RicercaAnnunciEffettuataService ricercaAnnunciEffettuataService;
    private final CategoriaNotificaRepository categoriaNotificaRepository;

    public ResponseEntity<String> inviaNotificaPromozionale(NotificaPromozionaleRequest request){
        int count = 0;

        List<User> destinatari = getDestinatariNotifica(request.getCriteriDiRicerca());
        String mittente = getNomeAzenziaImmobiliare();
        for(User destinatario : destinatari){
                try{
                    boolean inviato = inviaNotifica(CategoriaNotificaName.PROMOZIONI,destinatario,mittente,DatiContenutoNotificaPromozioni.fromRequest(request));

                    if(inviato)count++;

                }catch(Exception e){

                }

        }

        return ResponseEntity.ok("numero di notifiche inviate: " + count);
    }

    public void inviaNotificaPerNuovoAnnuncioImmobiliare(AnnuncioImmobiliare annuncio) {
       CriteriDiRicercaUtenti criteri=CriteriDiRicercaUtenti.map(annuncio);
        List<User> destinatari = getDestinatariNotifica(criteri);
        for(User destinatario : destinatari){
            DatiContenutoImmobile dati = DatiContenutoImmobile.fromAnnuncio(annuncio, destinatario);
           try {
               inviaNotifica(CategoriaNotificaName.OPPORTUNITA_IMMOBILE,destinatario,"DietiEstate",dati);
           }catch (Exception e){
               //non fare nulla
           }
        }

    }

    private List<User> getDestinatariNotifica(CriteriDiRicercaUtenti request) {
        return  ricercaAnnunciEffettuataService.UtentiInteressati(request);
    }

    private String getNomeAzenziaImmobiliare(){
        AgenziaImmobiliare agenzia =agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(UserContex.getUserCurrent())
                .orElseThrow(() -> new UnauthorizedException("Permesso negato.\n L'utente non è un dipendente di un'agenzia immobiliare"));

        return "promozioni@"+agenzia.getDominio()+".dietiEstate.it";
    }

    public Integer getNumeroAllNotifiche(){

        User userCurrent = UserContex.getUserCurrent();

        return notificaRepository.countByDestinatario(userCurrent);
    }

    public List<NotificaResponse> getAllNotifiche(FiltroNotificaRequest request){

        User userCurrent = UserContex.getUserCurrent();

        Pageable pageable = getPaginableNotifiche(request);

        List<Notifica> notifiche = notificaRepository.findAllByDestinatario(userCurrent,pageable);

        return getListNotificaResponseFromListaNotifica(notifiche);
    }

    private Pageable getPaginableNotifiche(FiltroNotificaRequest request){

        Pageable pageable;

        if(request.isOrdinatiPerDataDesc()){

            pageable = PageRequest.of(request.getNumeroPagina(),request.getNumeroDiElementiPerPagina(), Sort.by("dataCreazione").descending());

        }else{

            pageable = PageRequest.of(request.getNumeroPagina(),request.getNumeroDiElementiPerPagina(), Sort.by("dataCreazione").ascending());
        }

        return pageable;
    }

    private List<NotificaResponse> getListNotificaResponseFromListaNotifica(List<Notifica> notifiche){

        List<NotificaResponse> notificheResponse = new ArrayList<>();

        for(Notifica notifica : notifiche){
            NotificaResponse notificaResponse = new NotificaResponse();
            notificaResponse.setOggetto(notifica.getOggetto());
            notificaResponse.setId(notifica.getId());
            notificaResponse.setContenuto(notifica.getContenuto());
            notificaResponse.setMittente(notifica.getMittente());
            notificaResponse.setDataDiCreazione(notifica.getDataCreazione());
            notificaResponse.setLetta(notifica.isLetta());
            notificaResponse.setIdCategoria(notifica.getCategoria().getId());
            notificheResponse.add(notificaResponse);
        }

        return notificheResponse;
    }

    public Integer getNumeroNotificheByCategoria(int idCategoria){

        User userCurrent = UserContex.getUserCurrent();

        CategoriaNotifica categoriaNotifica = CategoriaNotifica.builder()
                .id(idCategoria)
                .build();

        return notificaRepository.countByDestinatarioAndCategoria(userCurrent,categoriaNotifica);
    }

    public List<NotificaResponse> getNotificheByCategoria(FiltroNotificaRequest request){

        User userCurrent = UserContex.getUserCurrent();

        Pageable pageable = getPaginableNotifiche(request);

        CategoriaNotifica categoriaNotifica = CategoriaNotifica.builder()
                .id(request.getIdCategoria())
                .build();

        List<Notifica> notifiche = notificaRepository.findAllByDestinatarioAndCategoria(userCurrent,categoriaNotifica,pageable);

        return getListNotificaResponseFromListaNotifica(notifiche);
    }

    private DatiImpiegato getDatiImpiegato(Proposta proposta) {
        return datiImpiegatoRepository.findByUser_Id(proposta.getAnnuncio().getAgente().getId())
                .orElseThrow(() -> new UnauthorizedException("Permesso negato.\n L'utente non è un impiegato"));
    }

    public void inviaNotificaControproposta( Proposta proposta) {
        DatiContenutoControproposta dati = DatiContenutoControproposta.fromProposta(proposta, getDatiImpiegato(proposta));
        inviaNotifica(CategoriaNotificaName.CONTROPROPOSTA, proposta.getUser(),"info@dieti.estate.it", dati);
    }

    public void inviaNotificaAccettazione( Proposta proposta) {
        DatiContenutoPropostaAccettata dati = DatiContenutoPropostaAccettata.fromProposta(proposta, getDatiImpiegato(proposta));
        inviaNotifica(CategoriaNotificaName.PROPOSTA_ACCETTATA, proposta.getUser(), "info@dieti.estate.it",dati);
    }

    public void inviaNotificaRifiuto(Proposta proposta) {
        DatiContenutoPropostaRifiutata dati = DatiContenutoPropostaRifiutata.fromProposta(proposta);
        inviaNotifica(CategoriaNotificaName.PROPOSTA_RIFIUTATA, proposta.getUser(), "info@dieti.estate.it",dati);
    }

    private <T extends DatiContenutoNotifica> boolean inviaNotifica(CategoriaNotificaName tipoNotifica, User destinatario,String mittente, T dati) {
        CategoriaNotifica categoria = recuperaCategoria(tipoNotifica);
        if(destinatario == null || destinatario.getCategorieDisattivate().contains(categoria)){
            return false;
        }

        // Ottieni il generatore di contenuto tipizzato in base al tipo di notifica
        GeneratoreContenutoNotifica<T> generatore = GeneratoreContenutoFactory.getGeneratore(tipoNotifica);
        // Genera il contenuto HTML
        String contenutoHtml = generatore.generaContenuto(dati);


        // Creazione della notifica utilizzando il builder
        Notifica notifica = Notifica.builder()
                .oggetto(generatore.generaOggetto(dati))
                .contenuto(contenutoHtml)
                .dataCreazione(LocalDateTime.now())
                .mittente(mittente) 
                .categoria(categoria)
                .destinatario(destinatario)
                .build();

        // Salva la notifica nel database
        notificaRepository.save(notifica);
        return true;
    }

    private CategoriaNotifica recuperaCategoria(CategoriaNotificaName tipoNotifica) {
        return categoriaNotificaRepository.findByCategoriaName(tipoNotifica).orElseThrow(() ->new IllegalArgumentException("Tipo di notifica non riconosciuto: " + tipoNotifica));
    }

    public String setTrueVisualizzazioneNotifica(int idNotifica){

        Notifica notifica = notificaRepository.findById(idNotifica).get();

        checkIsPropretarioNotifica(notifica);

        notifica.setLetta(true);

        notificaRepository.save(notifica);

        return "Notifica settata come letta";
    }

    private void checkIsPropretarioNotifica(Notifica notifica){

        User userCurrent = UserContex.getUserCurrent();

        User proprietarioNotifica = notifica.getDestinatario();

        if(!(userCurrent.equals(proprietarioNotifica))){

            throw new AccessDeniedException("Permesso negato.\n L'utente che vuole settare letta la notifica non è il propretario della notifica.");
        }
    }
}