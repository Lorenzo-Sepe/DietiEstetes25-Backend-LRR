package it.unina.dietiestates25.service.specification;

import it.unina.dietiestates25.dto.request.FiltroAnnuncioDTO;
import it.unina.dietiestates25.entity.*;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class AnnuncioImmobiliareSpecification {

    private AnnuncioImmobiliareSpecification(){

    }

    private static final String FIELD_IMMOBILE = "immobile";
    private static final String FIELD_CONTRATTO = "contratto";
    private static final String FIELD_PREZZO_AFFITTO = "prezzoAffitto";
    private static final String FIELD_PREZZO_VENDITA = "prezzoVendita";

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
            Join<AnnuncioImmobiliare, Immobile> immobile = root.join(FIELD_IMMOBILE);
            return cb.equal(immobile.get("tipologiaImmobile"), tipologia);
        };
    }

    public static Specification<AnnuncioImmobiliare> conTipoContratto(String tipoContratto) {
        return (root, query, cb) -> {
            if (tipoContratto == null || tipoContratto.isEmpty()) {
                return null;
            }
            Join<AnnuncioImmobiliare, Contratto> contratto = root.join(FIELD_CONTRATTO);
            return cb.equal(cb.lower(contratto.get("tipoContratto")), tipoContratto.toLowerCase());
        };
    }
    public static Specification<AnnuncioImmobiliare> conRangePrezzo(Double prezzoMin, Double prezzoMax) {
        return (root, query, cb) -> {
            if (prezzoMin == null && prezzoMax == null) {
                return null;
            }

            // Creiamo il join con la tabella del Contratto
            Join<AnnuncioImmobiliare, Contratto> contratto = root.join(FIELD_CONTRATTO);

            // Definiamo le espressioni per il prezzo
            Expression<Double> prezzoAffitto = contratto.get(FIELD_PREZZO_AFFITTO);
            Expression<Double> prezzoVendita = contratto.get(FIELD_PREZZO_VENDITA);

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
            Join<AnnuncioImmobiliare, Immobile> immobile = root.join(FIELD_IMMOBILE);

            Expression<Integer> metriQuadri =  immobile.get("metriQuadri");
            Predicate rangeMetriQuadri = null;

            if (minMq != null && maxMq != null) {
                // Per l'affitto
                rangeMetriQuadri = cb.between(metriQuadri, minMq, maxMq);
            } else if (minMq!= null) {
                // Per l'affitto
                rangeMetriQuadri = cb.greaterThanOrEqualTo(metriQuadri, minMq);
            } else {
                // Per l'affitto
                rangeMetriQuadri = cb.lessThanOrEqualTo(metriQuadri, maxMq);
            }

            return rangeMetriQuadri;
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

            Join<AnnuncioImmobiliare, Immobile> immobile = root.join(FIELD_IMMOBILE);
            Join<Immobile, Indirizzo> indirizzo = immobile.join("indirizzo");

            Predicate filtroLat = cb.between(indirizzo.get("latitudine"), latMin, latMax);
            Predicate filtroLon = cb.between(indirizzo.get("longitudine"), lonMin, lonMax);

            return cb.and(filtroLat, filtroLon);
        };
    }

    public static Specification<AnnuncioImmobiliare> conProvincia(String provincia){
        return (root, query, cb) -> {
            if (provincia == null || provincia.isEmpty()) {
                return null;
            }
            Join<AnnuncioImmobiliare, Immobile> immobile = root.join(FIELD_IMMOBILE);
            Join<Immobile, Indirizzo> indirizzo = immobile.join("indirizzo");

            return cb.like(cb.lower(indirizzo.get("citta")), "%" + provincia.toLowerCase() + "%");

        };
    }

    public static Specification<AnnuncioImmobiliare> conCaratteristicheAggiuntive(FiltroAnnuncioDTO filtro) {
        return (root, query, cb) -> {
            Join<AnnuncioImmobiliare, Immobile> immobile = root.join(FIELD_IMMOBILE);
            Join<Immobile, CaratteristicheAggiuntive> caratteristiche = immobile.join("caratteristicheAggiuntive");

            Predicate predicate = cb.conjunction();
            if (filtro.getBalconi() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("balconi"), filtro.getBalconi()));
            }
            if (filtro.getGarage() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("garage"), filtro.getGarage()));
            }
            if (filtro.getPostiAuto() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("postiAuto"), filtro.getPostiAuto()));
            }
            if (filtro.getGiardino() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("giardino"), filtro.getGiardino()));
            }
            if (filtro.getAscensore() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("ascensore"), filtro.getAscensore()));
            }
            if (filtro.getPortiere() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("portiere"), filtro.getPannelliSolari()));
            }
            if (filtro.getRiscaldamentoCentralizzato() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("riscaldamentoCentralizzato"), filtro.getRiscaldamentoCentralizzato()));
            }
            if (filtro.getClimatizzatori() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("climatizzatori"), filtro.getClimatizzatori()));
            }
            if (filtro.getPannelliSolari() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("pannelliSolari"), filtro.getPannelliSolari()));
            }
            if (filtro.getCantina() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("cantina"), filtro.getCantina()));
            }
            if (filtro.getSoffitta() != null) {
                predicate = cb.and(predicate, cb.equal(caratteristiche.get("soffitta"), filtro.getSoffitta()));
            }
            return predicate;
        };
    }

    public static Specification<AnnuncioImmobiliare> ordinaPerPrezzoAsc(boolean ordinePerPrezzoAsc, String tipoContratto) {

        return (root, query, cb) -> {

            if(!ordinePerPrezzoAsc){

                return null;
            }

            Join<AnnuncioImmobiliare, Contratto> contrattoJoin = root.join(FIELD_CONTRATTO);

            if(tipoContratto.equals("AFFITTO")){

                // downcast: specificare che vogliamo solo ContrattoAffitto
                query.orderBy(cb.asc(
                        cb.treat(contrattoJoin, ContrattoAffitto.class).get(FIELD_PREZZO_AFFITTO)
                ));

            } else {

                // downcast: specificare che vogliamo solo ContrattoVendita
                query.orderBy(cb.asc(
                        cb.treat(contrattoJoin, ContrattoVendita.class).get(FIELD_PREZZO_VENDITA)
                ));
            }

            return cb.conjunction();
        };
    }

    public static Specification<AnnuncioImmobiliare> ordinaPerPrezzoDesc(boolean isOrdinePerPrezzoDesc, String tipoContratto) {

        return (root, query, cb) -> {

            if(!isOrdinePerPrezzoDesc){

                return null;
            }

            Join<AnnuncioImmobiliare, Contratto> contrattoJoin = root.join(FIELD_CONTRATTO);

            if(tipoContratto.equals("AFFITTO")){

                // downcast: specificare che vogliamo solo ContrattoAffitto
                query.orderBy(cb.desc(
                        cb.treat(contrattoJoin, ContrattoAffitto.class).get(FIELD_PREZZO_AFFITTO)
                ));

            } else {

                // downcast: specificare che vogliamo solo ContrattoVendita
                query.orderBy(cb.desc(
                        cb.treat(contrattoJoin, ContrattoVendita.class).get(FIELD_PREZZO_VENDITA)
                ));
            }

            return cb.conjunction();
        };
    }

    public static Specification<AnnuncioImmobiliare> ordinaPerDataDesc(boolean isOrdinePerDataDesc) {

        return (root, query, cb) -> {

            if(!isOrdinePerDataDesc){

                return null;
            }

            query.orderBy(cb.desc(root.get("dataPubblicazione")));

            return cb.conjunction();
        };
    }

    public static Specification<AnnuncioImmobiliare> ordinaPerDataAsc(boolean isOrdinePerDataAsc) {

        return (root, query, cb) -> {

            if(!isOrdinePerDataAsc){

                return null;
            }

            query.orderBy(cb.asc(root.get("dataPubblicazione")));

            return cb.conjunction();
        };
    }
}
