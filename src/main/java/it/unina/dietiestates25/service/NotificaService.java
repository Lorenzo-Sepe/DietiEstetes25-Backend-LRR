package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.FiltroNotificaRequest;
import it.unina.dietiestates25.dto.request.NotificaPromozionaleRequest;
import it.unina.dietiestates25.dto.response.NotificaResponse;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.CategoriaNotificaName;
import it.unina.dietiestates25.exception.UnauthorizedException;
import it.unina.dietiestates25.factory.GeneratoreContenutoFactory;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoControproposta;
import it.unina.dietiestates25.factory.notifica.dati.DatiContenutoNotifica;
import it.unina.dietiestates25.repository.AgenziaImmobiliareRepository;
import it.unina.dietiestates25.repository.NotificaRepository;
import it.unina.dietiestates25.strategy.GeneratoreContenutoNotifica;
import it.unina.dietiestates25.utils.UserContex;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotBlank;
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

    private final EntityManager entityManager;
    private final AgenziaImmobiliareRepository agenziaImmobiliareRepository;

    public ResponseEntity<String> inviaNotificaPromozionale(NotificaPromozionaleRequest request){


        int count = 0;

        List<User> destinatari = getDestinatariNotifica(request.getAreaDiInteresse(), request.getTipoDiContrattoDiInteresse(), request.getTipologiaDiImmobileDiInteresse());

        for(User destinatario : destinatari){

            try{

                Notifica notifica = Notifica.builder().contenuto(request.getContenuto())
                        .dataCreazione(LocalDateTime.now())
                        .mittente(getNomeAzenziaImmobiliare())
                        .contenuto(request.getContenuto())
                        .destinatario(destinatario)
                        .build();

                notificaRepository.save(notifica);

                count++;

            }catch(Exception e){

            }
        }

        return ResponseEntity.ok("numero di notifiche inviate: " + count);
    }

    private String getNomeAzenziaImmobiliare(){
        AgenziaImmobiliare agenzia =agenziaImmobiliareRepository.findAgenziaImmobiliareByDipendentiContains(UserContex.getUserCurrent())
                .orElseThrow(() -> new UnauthorizedException("Permesso negato.\n L'utente non è un dipendente di un'agenzia immobiliare"));

        return "promozioni@"+agenzia.getDominio()+".dietiEstate.it";
    }

    private List<User> getDestinatariNotifica(@NotBlank String areaDiInteresse, String tipoDiContrattoDiInteresse, String tipologiaDiImmobileDiInteresse){

        User utente = entityManager.getReference(User.class, 1);
        User utente2 = entityManager.getReference(User.class, 2);

        List<User> destinatari = new ArrayList<>();

        destinatari.add(utente);
        destinatari.add(utente2);

        return destinatari;
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
            notificaResponse.setId(notifica.getId());
            notificaResponse.setContenuto(notifica.getContenuto());
            notificaResponse.setMittente(notifica.getMittente());
            notificaResponse.setDataDiCreazione(notifica.getDataCreazione());
            notificaResponse.setLetta(notifica.isLetta());
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


    public void inviaNotificaControproposta(User destinatario, Proposta proposta, DatiImpiegato datiImpiegato) {
        DatiContenutoControproposta dati = DatiContenutoControproposta.fromProposta(proposta, datiImpiegato);
        inviaNotifica(CategoriaNotificaName.CONTROPROPOSTA, destinatario, dati);
    }


    private <T extends DatiContenutoNotifica> void inviaNotifica(CategoriaNotificaName tipoNotifica, User destinatario, T dati) {
        // Ottieni il generatore di contenuto tipizzato in base al tipo di notifica
        GeneratoreContenutoNotifica<T> generatore = GeneratoreContenutoFactory.getGeneratore(tipoNotifica);
        // Genera il contenuto HTML
        String contenutoHtml = generatore.generaContenuto(dati);

        // Creazione della notifica utilizzando il builder
        Notifica notifica = Notifica.builder()
                .contenuto(contenutoHtml)
                .dataCreazione(LocalDateTime.now())
                .mittente("Sistema") // oppure il mittente dinamico
                // ATTENZIONE: il campo categoria nella classe Notifica è mappato come entità.
                //TODO
                // Occorrerebbe convertire il TipoNotifica in CategoriaNotifica oppure gestirlo opportunamente.
                .categoria(null)
                .destinatario(destinatario)
                .build();

        // Salva la notifica nel database
        notificaRepository.save(notifica);
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