package it.unina.dietiestates25.dto.request;

import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import lombok.Data;

@Data
public class FiltroAnnuncio {
    private String titolo;
    private TipologiaImmobile tipologiaImmobile;
    private Double prezzoMin;
    private Double prezzoMax;
    private Integer metriQuadriMin;
    private Integer metriQuadriMax;
    private Double latCentro;
    private Double lonCentro;
    private Double raggioKm;
    private Boolean balconi;
    private Boolean garage;
    private Boolean pannelliSolari;

}

