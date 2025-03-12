package it.unina.dietiestates25.dto.request;


import lombok.Data;

@Data
public class PageableProposte {

    int numeroPagina;

    int numeroElementiPerPagina;

    boolean isOrdinatiPerDataDesc;
}
