package it.unina.dietiestates25.dto.request;

import it.unina.dietiestates25.entity.enumeration.TipoContratto;
import it.unina.dietiestates25.entity.enumeration.TipologiaImmobile;
import lombok.Data;

@Data
public class FiltroAnnuncioDTO {
    //pageable
    private Integer numeroPagina;
    private Integer numeroDiElementiPerPagina;

    //Criteri d'ordine
    private boolean ordinePrezzoAsc;
    private boolean ordinePrezzoDesc;
    private boolean ordineDataAsc;
    private boolean ordineDataDesc;

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

    //Caratteristiche aggiuntive
    private Boolean balconi;
    private Boolean garage;
    private Boolean postiAuto;
    private Boolean giardino;
    private Boolean ascensore;
    private Boolean portiere;
    private Boolean riscaldamentoCentralizzato;
    private Boolean climatizzatori;
    private Boolean pannelliSolari;
    private Boolean cantina;
    private Boolean soffitta;
    private Boolean descrizioneAggiuntiva;


    //Dati staff
    private String agenteCreatoreAnnuncio;

}

