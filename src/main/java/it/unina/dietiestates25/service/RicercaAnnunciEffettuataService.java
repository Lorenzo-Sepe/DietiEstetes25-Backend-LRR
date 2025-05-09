package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.CriteriDiRicercaUtenti;
import it.unina.dietiestates25.dto.request.FiltroAnnuncio;
import it.unina.dietiestates25.entity.RicercaAnnunciEffettuata;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.exception.ResourceNotFoundException;
import it.unina.dietiestates25.repository.RicercaAnnunciEffettuataRepository;
import it.unina.dietiestates25.utils.SerializzazioneUtils;
import it.unina.dietiestates25.utils.UserContex;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RicercaAnnunciEffettuataService {

    private RicercaAnnunciEffettuataRepository repository;

    public void salvaRicercaAnnunciEffettuata(FiltroAnnuncio filtro) {
        User user= UserContex.getUserCurrent();
        RicercaAnnunciEffettuata ricerca= RicercaAnnunciEffettuata.builder().utente(user).build();
        List <String> localita= getLocalita(filtro);
        ricerca.setLocalita(localita);
        if(filtro.getPrezzoMin() ==null  || filtro.getPrezzoMin()<0)
            ricerca.setPrezzoMin(null);
        else
            ricerca.setPrezzoMin(BigDecimal.valueOf(filtro.getPrezzoMin()));

        if (filtro.getPrezzoMax() == null || filtro.getPrezzoMax()<0)
            ricerca.setPrezzoMax(null);
        else
            ricerca.setPrezzoMax(BigDecimal.valueOf(filtro.getPrezzoMax()));

        ricerca.setTipologiaImmobile(filtro.getTipologiaImmobile());
        ricerca.setTipologiaContratto(filtro.getTipologiaContratto());

        ricerca.setFiltoUsatoJson(SerializzazioneUtils.serializzaFiltroAnnuncio(filtro));
        
        if(checkIsSameRicerca(filtro)){
            int idUltimaRicerca= repository.findFirstByUtenteOrderByUpdatedAtDesc(user).get().getId();
            ricerca.setId(idUltimaRicerca);
        }
        try {
        repository.save(ricerca);
        }catch (Exception e) {
            //non fare nulla non serve che blocchi la ricerca}
        }
    }

   private boolean checkIsSameRicerca(FiltroAnnuncio filtro) {
    // Ottiene l'utente corrente
    User user = UserContex.getUserCurrent();

    // Recupera l'ultima ricerca effettuata dall'utente
    RicercaAnnunciEffettuata ricerca = repository.findFirstByUtenteOrderByUpdatedAtDesc(user).orElse(null);

    // Se non esiste alcuna ricerca precedente, restituisce false
    if (ricerca == null) {
        return false;
    }

    // Confronta la tipologia di immobile
    if (ricerca.getTipologiaImmobile() != filtro.getTipologiaImmobile()) {
        return false;
    }

    // Confronta il tipo di contratto
    if (ricerca.getTipologiaContratto() != filtro.getTipologiaContratto()) {
        return false;
    }

    // Confronta le località
    List<String> localita = getLocalita(filtro);
    if (localita.size() != ricerca.getLocalita().size()) {
        return false;
    }
    for (String loc : localita) {
        if (!ricerca.getLocalita().contains(loc)) {
            return false;
        }
    }

    // Se tutte le condizioni sono soddisfatte, restituisce true
    return true;
}
    private List<String> getLocalita(FiltroAnnuncio filtro) {
        if (filtro.getProvincia()!=null && !filtro.getProvincia().isBlank()){
            return List.of(filtro.getProvincia());
        }

        Double latitudine= filtro.getLatCentro();
        Double longitudine= filtro.getLonCentro();
        Double raggio= filtro.getRaggioKm();
        if (latitudine!=null && longitudine!=null && raggio!=null){

        //TODO: implementare ricerca province in base a latitudine, longitudine e raggio
        return List.of("Napoli");
        }
        return List.of();
    }

    public List<RicercaAnnunciEffettuata> getStoricoRicerche() {
        User user= UserContex.getUserCurrent();
        return repository.findByUtente(user);
    }


    public List<User>UtentiInteressati(CriteriDiRicercaUtenti request){
        if(request.getIntervallogiorniStoricoRicerca()<=0)
            request.setIntervallogiorniStoricoRicerca(7);

        return repository.trovaUtentiPerCriteri(request.getBudgetMin(), request.getBudgetMax(), request.getAreaDiInteresse(), request.getTipoDiContrattoDiInteresse(), request.getTipologiaDiImmobileDiInteresse(), LocalDateTime.now().minusDays(request.getIntervallogiorniStoricoRicerca()));
    }


    public FiltroAnnuncio getFiltroRicerca(int id) {
        RicercaAnnunciEffettuata ricerca = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ricerca", "id", id));
        if(ricerca.getUtente().getId() !=UserContex.getUserCurrent().getId()){
            throw new AccessDeniedException("Non puoi visualizzare la ricerca di un altro utente");
        }
        return SerializzazioneUtils.deserializzaFiltroAnnuncio(ricerca.getFiltoUsatoJson());

    }
}