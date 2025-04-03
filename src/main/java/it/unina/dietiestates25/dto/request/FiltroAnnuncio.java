package it.unina.dietiestates25.dto.request;

import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import lombok.Data;

@Data
public class FiltroAnnuncio {
    //pageable
    private Integer numeroPagina;
    private Integer numeroDiElementiPerPagina;

    private String titolo;
    private TipologiaImmobile tipologiaImmobile;
    private TipoContratto tipologiaContratto;
    private Double prezzoMin;
    private Double prezzoMax;
    private Integer metriQuadriMin;
    private Integer metriQuadriMax;

    //posizione
    private String provincia;
    private Double latCentro;
    private Double lonCentro;
    private Double raggioKm;

    private Boolean balconi;
    private Boolean garage;
    private Boolean pannelliSolari;

    //Dati staff
    private String agenteCreatoreAnnuncio;

}

