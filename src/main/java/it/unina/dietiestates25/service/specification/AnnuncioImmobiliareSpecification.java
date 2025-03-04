package it.unina.dietiestates25.service.specification;

import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class AnnuncioImmobiliareSpecification {

    public static Specification<AnnuncioImmobiliare> conTitolo(String titolo) {
        return (root, query, cb) -> {
            if (titolo == null || titolo.isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("titolo")), "%" + titolo.toLowerCase() + "%");
        };
    }

    public static Specification<AnnuncioImmobiliare> conTipologiaImmobile(TipologiaImmobile tipologia) {
        return (root, query, cb) -> {
            if (tipologia == null) {
                return null;
            }
            Join<AnnuncioImmobiliare, Immobile> immobile = root.join("immobile");
            return cb.equal(immobile.get("tipologiaImmobile"), tipologia);
        };
    }

    public static Specification<AnnuncioImmobiliare> conRangePrezzo(Double prezzoMin, Double prezzoMax) {
        return (root, query, cb) -> {
            if (prezzoMin == null && prezzoMax == null) {
                return null;
            }
            Join<AnnuncioImmobiliare, Contratto> contratto = root.join("contratto");
            if (prezzoMin != null && prezzoMax != null) {
                return cb.between(contratto.get("prezzo"), prezzoMin, prezzoMax);
            }
            return prezzoMin != null ? cb.greaterThanOrEqualTo(contratto.get("prezzo"), prezzoMin)
                    : cb.lessThanOrEqualTo(contratto.get("prezzo"), prezzoMax);
        };
    }

    public static Specification<AnnuncioImmobiliare> conRangeMetriQuadri(Integer minMq, Integer maxMq) {
        return (root, query, cb) -> {
            if (minMq == null && maxMq == null) {
                return null;
            }
            Join<AnnuncioImmobiliare, Immobile> immobile = root.join("immobile");
            return cb.between(immobile.get("metriQuadri"), minMq, maxMq);
        };
    }

    public static Specification<AnnuncioImmobiliare> conLocalizzazione(Double lat, Double lon, Double raggioKm) {
        return (root, query, cb) -> {
            if (lat == null || lon == null || raggioKm == null) {
                return null;
            }

            // Approssimazione: 1 grado di latitudine ≈ 111 km
            double raggioGradi = raggioKm / 111.0;
            double latMin = lat - raggioGradi;
            double latMax = lat + raggioGradi;
            double lonMin = lon - (raggioGradi / Math.cos(Math.toRadians(lat)));
            double lonMax = lon + (raggioGradi / Math.cos(Math.toRadians(lat)));

            Join<AnnuncioImmobiliare, Immobile> immobile = root.join("immobile");
            Join<Immobile, Indirizzo> indirizzo = immobile.join("indirizzo");

            Predicate filtroLat = cb.between(indirizzo.get("latitudine"), latMin, latMax);
            Predicate filtroLon = cb.between(indirizzo.get("longitudine"), lonMin, lonMax);

            return cb.and(filtroLat, filtroLon);
        };
    }

    public static Specification<AnnuncioImmobiliare> conCaratteristicheAggiuntive(Boolean balconi, Boolean garage, Boolean pannelliSolari) {
        return (root, query, cb) -> {
            Join<AnnuncioImmobiliare, Immobile> immobile = root.join("immobile");
            Join<Immobile, CaratteristicheAggiuntive> caratteristiche = immobile.join("caratteristicheAggiuntive");

            Predicate predicate = cb.conjunction();
            if (balconi != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("balconi"), balconi));
            }
            if (garage != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("garage"), garage));
            }
            if (pannelliSolari != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("pannelliSolari"), pannelliSolari));
            }
            return predicate;
        };
    }
}
