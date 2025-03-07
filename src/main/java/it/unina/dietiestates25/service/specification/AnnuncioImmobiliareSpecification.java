package it.unina.dietiestates25.service.specification;

import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

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

            // Creiamo il join con la tabella del Contratto
            Join<AnnuncioImmobiliare, Contratto> contratto = root.join("contratto");

            // Definiamo le espressioni per il prezzo
            Expression<Double> prezzoAffitto = contratto.get("prezzoAffitto");
            Expression<Double> prezzoVendita = contratto.get("prezzoVendita");

            // Predicati per il range di prezzo
            Predicate rangeAffitto = null;
            Predicate rangeVendita = null;

            if (prezzoMin != null && prezzoMax != null) {
                // Per l'affitto
                rangeAffitto = cb.between(prezzoAffitto, prezzoMin, prezzoMax);
                // Per la vendita
                rangeVendita = cb.between(prezzoVendita, prezzoMin, prezzoMax);
            } else if (prezzoMin != null) {
                // Per l'affitto
                rangeAffitto = cb.greaterThanOrEqualTo(prezzoAffitto, prezzoMin);
                // Per la vendita
                rangeVendita = cb.greaterThanOrEqualTo(prezzoVendita, prezzoMin);
            } else if (prezzoMax != null) {
                // Per l'affitto
                rangeAffitto = cb.lessThanOrEqualTo(prezzoAffitto, prezzoMax);
                // Per la vendita
                rangeVendita = cb.lessThanOrEqualTo(prezzoVendita, prezzoMax);
            }

            // Se ci sono condizioni sul prezzo, combiniamo i predicati
            if (rangeAffitto != null && rangeVendita != null) {
                // Concateniamo le condizioni per entrambi i tipi di contratto
                return cb.or(rangeAffitto, rangeVendita);
            } else if (rangeAffitto != null) {
                return rangeAffitto;
            } else if (rangeVendita != null) {
                return rangeVendita;
            }

            return null;
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
