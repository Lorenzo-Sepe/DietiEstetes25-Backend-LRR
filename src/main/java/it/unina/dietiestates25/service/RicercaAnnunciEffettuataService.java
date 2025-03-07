package it.unina.dietiestates25.service;

import it.unina.dietiestates25.entity.RicercaAnnunciEffettuata;
import it.unina.dietiestates25.repository.RicercaAnnunciEffettuataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

@Service
public class RicercaAnnunciEffettuataService {

    @Autowired
    private RicercaAnnunciEffettuataRepository repository;

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