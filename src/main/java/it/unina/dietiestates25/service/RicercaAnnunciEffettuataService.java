package it.unina.dietiestates25.service;

import it.unina.dietiestates25.dto.request.FiltroAnnuncio;
import it.unina.dietiestates25.entity.RicercaAnnunciEffettuata;
import it.unina.dietiestates25.entity.User;
import it.unina.dietiestates25.repository.RicercaAnnunciEffettuataRepository;
import it.unina.dietiestates25.utils.UserContex;
import lombok.AllArgsConstructor;
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
        ricerca.setDataRicerca(LocalDateTime.now());
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
        repository.save(ricerca);
    }

    private List<String> getLocalita(FiltroAnnuncio filtro) {
        if (filtro.getProvincia()!=null && filtro.getProvincia().isBlank()){
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

    /*public List<RicercaAnnunciEffettuata> cercaAnnunci(String localita, BigDecimal prezzoMin, BigDecimal prezzoMax) {
        Specification<RicercaAnnunciEffettuata> spec = (root, query, criteriaBuilder) -> {
            Predicate localitaPredicate;
            if (localita == null || localita.isEmpty()) {
                localitaPredicate = (Predicate) criteriaBuilder.conjunction();
            } else {
                localitaPredicate = (Predicate) criteriaBuilder.isMember(localita, root.get("localita"));
            }

            Predicate prezzoPredicate = (Predicate) criteriaBuilder.conjunction();
            if (prezzoMin != null) {
                prezzoPredicate = criteriaBuilder.and(prezzoPredicate, criteriaBuilder.greaterThanOrEqualTo(root.get("prezzoMin"), prezzoMin));
            }
            if (prezzoMax != null) {
                prezzoPredicate = criteriaBuilder.and(prezzoPredicate, criteriaBuilder.lessThanOrEqualTo(root.get("prezzoMax"), prezzoMax));
            }

            return criteriaBuilder.and(localitaPredicate, prezzoPredicate);
        };

        return repository.findAll(spec);
    }*/


}